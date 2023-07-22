package cn.wenhe9.pinda.core.log.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.wenhe9.pinda.core.log.entity.OptLogDto;
import cn.wenhe9.pinda.core.log.enums.LogTypeEnum;
import cn.wenhe9.pinda.core.log.event.OptLogEvent;
import cn.wenhe9.pinda.core.result.ResultResponse;
import cn.wenhe9.pinda.core.utils.CusAccessObjectUtil;
import cn.wenhe9.pinda.core.utils.LogInfoUtils;
import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @description: 操作日志切面
 * @author: DuJinliang
 * @create: 2023/7/22
 */
@Slf4j
@Aspect
@ConditionalOnWebApplication
@ConditionalOnProperty(name = "pinda.log.enabled", havingValue = "true", matchIfMissing = true)
public class OptLogAspect {
    private final ApplicationContext applicationContext;

    private static final ThreadLocal<OptLogDto> OPT_LOG_TL = new ThreadLocal<>();

    private static final String DASH = "-";

    public OptLogAspect(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Pointcut("@annotation(cn.wenhe9.pinda.core.log.annotation.OptLog)")
    public void optLogPointCut() {
    }

    @Before("optLogPointCut()")
    public void recordLogBefore(JoinPoint joinPoint)  {
        OptLogDto optLog = getOptLog();

        optLog.setCreateUser(null);
        optLog.setUserName("");
        optLog.setDescription(
                generateOptDesc(joinPoint)
        );
        optLog.setClassPath(joinPoint.getTarget().getClass().getName());
        optLog.setActionMethod(joinPoint.getSignature().getName());
        fillRequestParams(optLog, joinPoint);
        optLog.setStartTime(LocalDateTime.now());

        OPT_LOG_TL.set(optLog);
    }

    @AfterReturning(returning = "obj", pointcut = "optLogPointCut()")
    public void recordLogBefore(Object obj) {
        OptLogDto optLog = getOptLog();

        if (Objects.isNull(obj)) {
            optLog.setType(LogTypeEnum.OPT.type());
        } else {
            ResultResponse result = (ResultResponse) obj;
            if (result.isSuccess()) {
                optLog.setType(LogTypeEnum.OPT.type());
            } else {
                optLog.setType(LogTypeEnum.EX.type());
                optLog.setExDetail(result.getMessage());
            }
        }

        publishEvent(optLog);
        OPT_LOG_TL.remove();
    }

    @AfterThrowing(pointcut = "optLogPointCut()", throwing = "e")
    public void doAfterThrowable(Throwable e) {
        OptLogDto optLog= getOptLog();
        optLog.setType(LogTypeEnum.EX.type());

        optLog.setExDetail(LogInfoUtils.errInfo(e));
        // 异常信息
        optLog.setExDesc(e.getMessage());

        publishEvent(optLog);
        OPT_LOG_TL.remove();
    }

    private void publishEvent(OptLogDto optLog) {
        optLog.setFinishTime(LocalDateTime.now());
        optLog.setConsumingTime(optLog.getStartTime().until(optLog.getFinishTime(), ChronoUnit.MILLIS));
        applicationContext.publishEvent(new OptLogEvent(optLog));
    }

    private void fillRequestParams(OptLogDto optLog, JoinPoint joinPoint) {
        Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .ifPresent(requestAttributes -> {
                    HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                    String strArgs = "";
                    Object[] args = joinPoint.getArgs();

                    try {
                        // 判断不是文件上传类型的请求
                        if (notMultipartForData(request.getContentType())) {
                            strArgs = JSONObject.toJSONString(args);
                        }
                    } catch (Exception e) {
                        strArgs = Arrays.toString(args);
                    }

                    optLog.setParams(strArgs);

                    optLog.setRequestIp(CusAccessObjectUtil.getIpAddress(request));
                    optLog.setRequestUri(URLUtil.getPath(request.getRequestURI()));
                    optLog.setHttpMethod(request.getMethod());
                    optLog.setUa(StrUtil.sub(request.getHeader("user-agent"), 0, 500));
                });
    }

    private boolean notMultipartForData(String contentType) {
        return !contentType.contains(ContentType.MULTIPART_FORM_DATA.getMimeType());
    }

    private String generateOptDesc(JoinPoint joinPoint) {
        StringBuffer sb = new StringBuffer();

        Tag tag = joinPoint.getTarget().getClass().getAnnotation(Tag.class);
        Optional.ofNullable(tag).ifPresent(tagEx ->{
            sb.append(tagEx.name());
        });

        MethodSignature signature  = (MethodSignature) joinPoint.getSignature();
        Operation operation = signature.getMethod().getAnnotation(Operation.class);
        Optional.ofNullable(operation).ifPresent(optEx -> {
            sb.append(DASH).append(optEx.summary());
        });

        return sb.toString();
    }

    private OptLogDto getOptLog() {
        return Optional.ofNullable(OPT_LOG_TL.get())
                .orElse(new OptLogDto());
    }
}
