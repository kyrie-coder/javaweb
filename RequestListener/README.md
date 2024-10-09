## 概述

`MyServletRequestListener` 是一个实现了 `ServletRequestListener` 接口的 Servlet 请求监听器，用于记录每个 HTTP 请求的处理信息。此监听器会在请求初始化和销毁时执行相应的操作，以便收集请求的相关数据并输出日志。

## 注解

- `@WebListener`: 标识该类为一个 Servlet 监听器，Servlet 容器会自动注册它。

## 主要功能

1. **请求初始化 (`requestInitialized`)**:
   - 在每个请求被初始化时，记录请求开始的时间（毫秒）。
   - 将该时间存储在请求作用域中，以便后续计算请求处理的时间。
2. **请求销毁 (`requestDestroyed`)**:
   - 在每个请求被销毁时，执行以下操作：
     - 获取请求开始时间。
     - 记录请求结束时间。
     - 收集客户端 IP 地址、请求方法、请求 URI、查询字符串和 User-Agent。
     - 计算请求的处理时间，并将所有信息格式化为日志消息。
     - 输出日志消息到控制台。

## 代码实现细节

### 1. 请求初始化

```java
@Override
public void requestInitialized(ServletRequestEvent sre) {
    HttpServletRequest servletRequest = (HttpServletRequest)sre.getServletRequest();
    servletRequest.setAttribute("timeStart", System.currentTimeMillis());
}
```

- 将当前时间（请求开始时间）以毫秒为单位存入请求的属性 `timeStart`。

### 2. 请求销毁

```java
@Override
public void requestDestroyed(ServletRequestEvent sre) {
    HttpServletRequest servletRequest = (HttpServletRequest)sre.getServletRequest();
    long timeStart = (Long)servletRequest.getAttribute("timeStart");
    long timeEnd = System.currentTimeMillis();
    String clientIP = servletRequest.getRemoteAddr();
    String requestMethod = servletRequest.getMethod();
    String requestURI = servletRequest.getRequestURI();
    String queryString = servletRequest.getQueryString();
    String userAgent = servletRequest.getHeader("User-Agent");
    
    String logMessage = String.format("Time: %s, IP: %s, Method: %s, URI: %s, Query: %s, User-Agent: %s, Processing Time: %d ms",
            new Date(), clientIP, requestMethod, requestURI, queryString, userAgent, timeEnd - timeStart);
    System.out.println(logMessage);
}
```

- 在请求结束时，收集请求的各项信息并计算处理时间。
- 使用 `String.format` 方法格式化日志消息，便于阅读和分析。

## 日志输出示例

以下是日志输出的示例格式：

```java
Time: Wed Oct 09 21:12:10 CST 2024, IP: 0:0:0:0:0:0:0:1, Method: GET, URI: /servletrequestlistener/myServlet, Query: null, User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36, Processing Time: 0 ms
```

## 总结

`MyServletRequestListener` 是一个有效的工具，能够帮助开发者监控和记录 HTTP 请求的相关信息。这对于性能分析、问题诊断和审计日志非常有用。通过记录请求处理时间和客户端信息，开发者可以更好地理解应用的使用情况和性能瓶颈。