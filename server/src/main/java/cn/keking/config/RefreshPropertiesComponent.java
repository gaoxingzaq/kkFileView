package cn.keking.config;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.artofsolving.jodconverter.util.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 刷新配置
 *
 * @author zhongjiahua
 * @date 2022-11-29
 */
@Component
public class RefreshPropertiesComponent {
    private final static Logger log = LoggerFactory.getLogger(RefreshPropertiesComponent.class);
    private Map<String, Consumer<Properties>> configMap;

    public RefreshPropertiesComponent() {
        this.configMap = new HashMap<>(4);
        // 初始化
        configMap.put(ConfigUtils.getCustomizedConfigPath(), ConfigRefreshUtils::parseProperties);
    }

    @PostConstruct
    public void afterPropertiesSet() {
        // 预热一遍
        try {
            for (Map.Entry<String, Consumer<Properties>> entry : configMap.entrySet()) {
                Properties properties = new Properties();
                log.info("初始化【{}】配置文件", entry.getKey());
                try (FileInputStream fileInputStream = new FileInputStream(Paths.get(entry.getKey()).toFile())) {
                    properties.load(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
                    entry.getValue().accept(properties);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("加载配置文件错误", e);
        }
        Thread thread = new Thread(new ConfigRefreshThread(configMap));
        thread.start();
    }


    static class ConfigRefreshThread implements Runnable {
        private final Map<String, Consumer<Properties>> configMap;

        public ConfigRefreshThread(Map<String, Consumer<Properties>> configAttr) {
            this.configMap = configAttr;
        }

        @Override
        public void run() {
            List<Map.Entry<String, Consumer<Properties>>> entryConfigs = null;
            if (MapUtils.isNotEmpty(configMap)) {
                entryConfigs = configMap.entrySet().stream()
                        .filter(entry -> {
                            Path path = Paths.get(entry.getKey());
                            boolean exists = Files.exists(path) && path.toFile().isFile();
                            if (!exists) {
                                log.warn("找不到对应文件或非文件【{}】", path);
                            }
                            return exists;
                        })
                        .collect(Collectors.toList());
            }
            if (CollectionUtils.isEmpty(entryConfigs)) {
                return;
            }
            // 已注册的文件夹
            Set<String> hasRegisterDirs = new HashSet<>();
            // 已注册的文件
            Set<String> hasRegisterFiles = new HashSet<>();
            try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
                for (Map.Entry<String, Consumer<Properties>> entry : entryConfigs) {
                    Path filePath = Paths.get(entry.getKey());
                    hasRegisterFiles.add(filePath.toFile().getAbsolutePath());

                    File parentFile = filePath.toFile().getParentFile();
                    if (!hasRegisterDirs.contains(parentFile.getAbsolutePath())) {
                        Paths.get(parentFile.toURI()).register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.OVERFLOW);
                        hasRegisterDirs.add(parentFile.getAbsolutePath());
                    }
                }

                while (true) {
                    // 阻塞等待
                    WatchKey key = watchService.take();
                    Path path = (Path) key.watchable();
                    // 优先reset
                    if (!key.reset()) {
                        log.warn("【{}】不能再注册监听", path.toString());
                    }
                    String watchFilePath = null;
                    for (WatchEvent<?> pollEvent : key.pollEvents()) {
                        // 具体的文件名
                        Path changePath = (Path) pollEvent.context();
                        // 包装成路径
                        File file = path.resolve(changePath).toFile();
                        if (hasRegisterFiles.contains(file.getAbsolutePath())) {
                            watchFilePath = file.getAbsolutePath();
                            break;
                        }
                    }

                    if (Objects.nonNull(watchFilePath)) {
                        String copyWatchFilePath = watchFilePath;
                        Optional<Map.Entry<String, Consumer<Properties>>> consumerEntryOptional = entryConfigs.stream()
                                .filter(entry -> Paths.get(entry.getKey()).equals(Paths.get(copyWatchFilePath)))
                                .findFirst();
                        if (consumerEntryOptional.isPresent()) {
                            Map.Entry<String, Consumer<Properties>> entry = consumerEntryOptional.get();
                            Properties properties = new Properties();
                            log.info("更新【{}】配置文件", entry.getKey());
                            try (FileInputStream inStream = new FileInputStream(Paths.get(entry.getKey()).toFile())) {
                                properties.load(new InputStreamReader(inStream, StandardCharsets.UTF_8));
                                entry.getValue().accept(properties);
                            }
                        }
                    }

                }
            } catch (Exception e) {
                log.error("刷新配置失败 : " + e.getMessage(), e);
            }
        }
    }
}
