package cn.keking.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SecurityFilterProxy extends OncePerRequestFilter {


    @Value("${report.securityFilter:true}")
    private boolean securityFilter;

    @Autowired
    WebApplicationContext applicationContext;

    private static List<String> urlList=new ArrayList<>();

    @Value("${access.control.notAllow.methods:trace}")
    private String NOT_ALLOW_METHODS;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestUrl = request.getServletPath();
       // System.out.println(requestUrl);
        if(securityFilter) {
            if((","+NOT_ALLOW_METHODS+",").indexOf(","+request.getMethod().toLowerCase()+",")>-1){
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return ;
            }
        }
        super.doFilter(request, response, filterChain);
        return;
    }
}
