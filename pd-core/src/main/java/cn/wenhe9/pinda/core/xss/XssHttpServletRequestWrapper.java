package cn.wenhe9.pinda.core.xss;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @description: 用于过滤xss的httpServletRequest装饰器
 * @author: DuJinliang
 * @create: 2023/7/12
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private static final Map<String, String> REPLACE_RULE = new HashMap<>();

    static {
        REPLACE_RULE.put("<", "&lt;");
        REPLACE_RULE.put(">", "&gt;");
    }

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);

        if (Objects.nonNull(values)) {
            int length = values.length;

            // 不修改原数据
            String[] escapeValues = new String[length];
            for (int i = 0; i < length; i++) {
                escapeValues[i] = values[i];
                int index = i;
                REPLACE_RULE.forEach((k, v) -> escapeValues[index] = escapeValues[index].replaceAll(k, v));
            }
            return escapeValues;
        }
        return new String[0];
    }
}
