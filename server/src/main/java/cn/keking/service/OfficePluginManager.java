package cn.keking.service;

import com.sun.star.document.UpdateDocMode;
import org.apache.commons.lang3.StringUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.artofsolving.jodconverter.office.OfficeUtils;
import org.artofsolving.jodconverter.util.PlatformUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建文件转换器
 *
 * @author yudian-it
 * @date 2017/11/13
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OfficePluginManager {

    private final Logger logger = LoggerFactory.getLogger(OfficePluginManager.class);

    private OfficeManager officeManager;

    @Value("${office.plugin.server.ports:2001,2002}")
    private String serverPorts;

    @Value("${office.plugin.task.timeout:5m}")
    private String timeOut;

    /**
     * 启动Office组件进程
     */
    @Value("${office.officexh:1}")
    private String officexh;
    @PostConstruct
    public void initOfficeManager() {
       if (officexh.equals("2")) {
            System.out.println("启用开源poi转换");
        }else{
            new Thread(this::startOfficeManager).start();
        }

    }
    public void startOfficeManager(){
        File officeHome = OfficeUtils.getDefaultOfficeHome();
        if (officeHome == null) {
            throw new RuntimeException("找不到office组件，请确认'office.home'配置是否有误");
        }
        boolean killOffice = killProcess();
        if (killOffice) {
            logger.warn("检测到有正在运行的office进程，已自动结束该进程");
        }
        try {
            Thread.sleep(5 * 1000);
            DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
            configuration.setOfficeHome(officeHome);
            String []portsString = serverPorts.split(",");

            int[] ports = Arrays.stream(portsString).mapToInt(Integer::parseInt).toArray();

            configuration.setPortNumbers(ports);
            long timeout = DurationStyle.detectAndParse(timeOut).toMillis();
            // 设置任务执行超时为5分钟
            configuration.setTaskExecutionTimeout(timeout);
            // 设置任务队列超时为24小时
            //configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);
            officeManager = configuration.buildOfficeManager();
            officeManager.start();
        } catch (Exception e) {
            logger.error("启动office组件失败，请检查office组件是否可用");
            try {
                throw e;
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }

    public OfficeDocumentConverter getDocumentConverter() {
        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager, new OfficePluginExtendFormatRegistry());
        converter.setDefaultLoadProperties(getLoadProperties());
        return converter;
    }

    private Map<String,?> getLoadProperties() {
        Map<String,Object> loadProperties = new HashMap<>(10);
        loadProperties.put("Hidden", true);
        loadProperties.put("ReadOnly", true);
        loadProperties.put("UpdateDocMode", UpdateDocMode.QUIET_UPDATE);
        loadProperties.put("CharacterSet", StandardCharsets.UTF_8.name());
        return loadProperties;
    }

    private boolean killProcess() {
        boolean flag = false;
        try {
            if (PlatformUtils.isWindows()) {
                Process p = Runtime.getRuntime().exec("cmd /c tasklist ");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream os = p.getInputStream();
                byte[] b = new byte[256];
                while (os.read(b) > 0) {
                    baos.write(b);
                }
                String s = baos.toString();
                if (s.contains("soffice.bin")) {
                    Runtime.getRuntime().exec("taskkill /im " + "soffice.bin" + " /f");
                    flag = true;
                }
            } else if (PlatformUtils.isLinux()) {
                Process p = Runtime.getRuntime().exec(new String[]{"sh", "-c", "ps -ef | grep " + "soffice.bin" + " |grep -v grep | wc -l"});
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream os = p.getInputStream();
                byte[] b = new byte[256];
                while (os.read(b) > 0) {
                    baos.write(b);
                }
                String s = baos.toString();
                if (!"0".equals(s)) {
                    String[] cmd = {"sh", "-c", "ps -ef | grep soffice.bin | grep -v grep | awk '{print \"kill -9 \"$2}' | sh"};
                    Runtime.getRuntime().exec(cmd);
                    flag = true;
                }
            } else {
                Process p = Runtime.getRuntime().exec(new String[]{"sh", "-c", "ps -ef | grep " + "soffice.bin"});
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream os = p.getInputStream();
                byte[] b = new byte[256];
                while (os.read(b) > 0) {
                    baos.write(b);
                }
                String s = baos.toString();
                if (StringUtils.ordinalIndexOf(s, "soffice.bin", 3) > 0) {
                    String[] cmd = {"sh", "-c", "kill -15 `ps -ef|grep " + "soffice.bin" + "|awk 'NR==1{print $2}'`"};
                    Runtime.getRuntime().exec(cmd);
                    flag = true;
                }
            }
        } catch (IOException e) {
            logger.error("检测office进程异常", e);
        }
        return flag;
    }

    @PreDestroy
    public void destroyOfficeManager(){
        if (null != officeManager && officeManager.isRunning()) {
            logger.info("Shutting down office process");
            officeManager.stop();
        }
    }

}
