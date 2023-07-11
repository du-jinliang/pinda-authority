package cn.wenhe9.pinda.core.xss;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: xss过滤器
 * @author: DuJinliang
 * @create: 2023/7/12
 */
@Component
@EnableConfigurationProperties(XssProperties.class)
@WebFilter(urlPatterns = "/*", filterName = "xssFilter")
@ConditionalOnProperty(name = "pinda.xss.enabled", havingValue = "true")
public class XssFilter implements Filter {

    private final XssProperties xssProperties;

    public XssFilter(XssProperties xssProperties) {
        this.xssProperties = xssProperties;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        if (handleExcludeUrl(servletRequest)) {
            chain.doFilter(servletRequest, response);
            return;
        }

        chain.doFilter(new XssHttpServletRequestWrapper(servletRequest), response);
    }

    private Boolean handleExcludeUrl(HttpServletRequest request) {
        if (!CollectionUtils.isEmpty(xssProperties.getExcludes())) {
            return xssProperties.getExcludes().stream()
                    .anyMatch(exclude -> {
                        Pattern pattern = Pattern.compile("^" + exclude);
                        Matcher matcher = pattern.matcher(request.getServletPath());
                        return matcher.find();
                    });
        }
        return Boolean.TRUE;
    }
}
