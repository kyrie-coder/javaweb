import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;

@WebListener
public class MyServletRequestListener implements ServletRequestListener{
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        //获取HttpServletRequest对象
        HttpServletRequest servletRequest = (HttpServletRequest)sre.getServletRequest();
        //获取当前系统时间,并存入request作用域中
        servletRequest.setAttribute("timeStart", System.currentTimeMillis());
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        //获取HttpServletRequest对象
        HttpServletRequest servletRequest = (HttpServletRequest)sre.getServletRequest();
        long timeStart = (Long)servletRequest.getAttribute("timeStart");  //获取request作用域中存入的请求开始时间
        long timeEnd = System.currentTimeMillis();  //获取请求销毁时的系统时间
        String clientIP = servletRequest.getRemoteAddr();  //获取客户端IP地址
        String requestMethod = servletRequest.getMethod();  //获取请求方法
        String requestURI = servletRequest.getRequestURI();  //获取请求URI
        String queryString = servletRequest.getQueryString();  //获取查询字符串
        String userAgent = servletRequest.getHeader("User-Agent");  //获取User-Agent
        //格式化日志
        String logMessage = String.format("Time: %s, IP: %s, Method: %s, URI: %s, Query: %s, User-Agent: %s, Processing Time: %d ms",
                new Date(), clientIP, requestMethod, requestURI, queryString, userAgent, timeEnd - timeStart);
        //输出日志
        System.out.println(logMessage);
    }
}
