package cn.keking.web.controller;

import cn.keking.config.ConfigConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *  页面跳转
 * @author yudian-it
 * @date 2017/12/27
 */

@Controller
public class IndexController {

    @GetMapping( "/index")
    public String go2Index(){
        if(ConfigConstants.getpreviewindex().equalsIgnoreCase("true")){
            return "index";
        }else {
            return "null";
        }
    }
    public static boolean isBase64(String str) {
        if (str == null || str.length() == 0) {
            return false;
        } else {
            if (str.length() % 4 != 0) {
                return false;
            }

            char[] strChars = str.toCharArray();
            for (char c:strChars) {
                if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')
                        || c == '+' || c == '/' || c == '=') {
                    continue;
                } else {
                    return false;
                }
            }
            return true;
        }
    }
    @GetMapping( "/")
    public String root() {
            return "redirect:/index";
    }
}
