`LoginFilter` 是一个用于验证用户是否已登录的 Servlet 过滤器。该过滤器会拦截所有请求，并根据用户的登录状态决定是否允许访问受保护的资源。未登录的用户将被重定向到登录页面。

## 主要功能

1. **请求拦截**：过滤器会拦截所有传入的 HTTP 请求。
2. **排除路径**：定义了一些不需要登录的路径（如登录、注册和公共资源）。
3. **用户验证**：检查用户的登录状态，如果用户已登录，则允许继续访问；如果未登录，则重定向到登录页面。

## 代码说明

### 1. 注解定义

```java
@WebFilter("/*")
```

- 使用 `@WebFilter` 注解定义该过滤器适用于所有路径（`/*`）。

### 2. 排除列表

```java
private static final String[] EXCLUSION_LIST = {"/login","/register","/public"};
```

- 定义了一个字符串数组 `EXCLUSION_LIST`，用于列出不需要进行登录验证的请求路径。

### 3. 初始化方法

```java
@Override
public void init(FilterConfig filterConfig) throws ServletException {
}
```

- `init` 方法在过滤器初始化时调用，可以用于进行一些配置或初始化操作，此处未实现任何内容。

### 4. 核心过滤逻辑

```java
@Override
public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    String servletPath = request.getServletPath();
    if(isExclusion(servletPath)){
        filterChain.doFilter(request, response);
        return;
    }

    HttpSession session = request.getSession();
    Object user = session.getAttribute("user");
    if (user != null) {
        filterChain.doFilter(request, response);
        return;
    } else {
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
```

- **请求转换**：将 `ServletRequest` 和 `ServletResponse` 转换为 `HttpServletRequest` 和 `HttpServletResponse` 以便处理 HTTP 特有的功能。
- **获取请求路径**：使用 `getServletPath()` 方法获取当前请求的 额外路径。
- **排除逻辑**：如果请求的 额外路径 在排除列表中，直接放行。
- **会话检查**：获取当前会话，并检查是否存在名为 `"user"` 的属性。如果该属性存在，表示用户已登录，允许继续；否则，重定向到登录页面。

### 5. 销毁方法

```java
@Override
public void destroy() {
}
```

- `destroy` 方法在过滤器销毁时调用，可以用于进行一些清理工作，此处未实现任何内容。

### 6. 排除检查方法

```java
private boolean isExclusion(String path){
    for (String exclusion : EXCLUSION_LIST){
        if (path.startsWith(exclusion)){
            return true;
        }
    }
    return false;
}
```

- **排除检查**：此方法遍历排除列表，检查请求的 额外路径 是否为任一排除路径。

## 总结

`LoginFilter` 通过简单的逻辑有效地控制访问权限，确保只有已登录用户能够访问受保护的资源，并提供了灵活的排除路径配置。这种方式可以提升应用的安全性，并改善用户体验。