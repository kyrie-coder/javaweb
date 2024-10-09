import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


@WebFilter("/*")
public class LoginFilter implements Filter {
    private static final String[] EXCLUSION_LIST = {"/login","/register","/public"};       //创建排除列表

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;       //将请求转为HttpServletRequest类型
        HttpServletResponse response = (HttpServletResponse) servletResponse;   //将响应转为HttpServletResponse类型

        String servletPath = request.getServletPath();        //获取额外请求路径
        if(isExclusion(servletPath)){
            filterChain.doFilter(request, response);        //请求路径在排除列表中，直接放行
            return;
        }

        HttpSession session = request.getSession();        //获取session
        Object user = session.getAttribute("user");    //获取登陆属性
        if (user != null) {
            filterChain.doFilter(request, response);        //用户已登陆，直接放行
            return;
        }
        else{
            response.sendRedirect(request.getContextPath() + "/login");        //用户未登陆，重定向到登陆页面
        }
    }

    @Override
    public void destroy() {

    }

    private boolean isExclusion(String path){        //判断请求路径是否在排除列表中
        for (String exclusion : EXCLUSION_LIST){
            if (path.startsWith(exclusion)){
                return true;        //请求路径在排除列表中，返回true
            }
        }
        return false;       //请求路径不在排除列表中，返回false
    }
}
