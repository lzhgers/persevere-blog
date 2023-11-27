package com.lzh.lzhblog.aspect;


import com.lzh.lzhblog.annotation.LogAnnotation;
import com.lzh.lzhframework.domain.entity.LogEntity;
import com.lzh.lzhframework.service.LogService;
import com.lzh.lzhframework.utils.IpUtils;
import com.lzh.lzhframework.utils.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static com.lzh.lzhframework.constants.SysConstants.BROWSER;
import static com.lzh.lzhframework.constants.SysConstants.OS;

/**
 * @author luzhiheng
 * @date 2023/6/6 12:13
 */
@Aspect
@Component
public class SystemLogAspect {

    @Resource
    private LogService logService;
    private static Logger LOGGER = LoggerFactory.getLogger(SystemLogAspect.class);

    /**
     * 定义切点@PointCut
     * 在注解位置切入代码
     */
    @Pointcut("@annotation(com.lzh.lzhblog.annotation.LogAnnotation)")
    public void logPointCut() {
    }

    //前置通知
    //在执行方法之前打印获取的参数内容
    @Before("logPointCut()")
    public void before(JoinPoint joinPoint) throws UnsupportedEncodingException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        request.setCharacterEncoding("UTF-8");

        LOGGER.info("URL: {}", request.getRequestURL().toString());
        LOGGER.info("HTTP请求类型: {}", request.getMethod());
        LOGGER.info("执行方法: {}", joinPoint);
        LOGGER.info("传递参数: {}", Arrays.toString(joinPoint.getArgs()));
        LOGGER.info("IP地址:   {}: ", request.getRemoteAddr());

        LogEntity logEntity = new LogEntity();
        String url = request.getRequestURL().toString();
        url = url.substring(21);
        System.out.println("请求接口：" + url);
        logEntity.setRequestInterface(url);

        String requestMethod = request.getMethod();
        System.out.println("获取请求方式" + requestMethod);
        logEntity.setRequestWay(requestMethod);

        String requestParams = Arrays.toString(joinPoint.getArgs());
        logEntity.setContent(requestParams);
        System.out.println("获取请求参数" + requestParams);

        //开始调用时间
        // 计时并调用目标函数
        long start = System.currentTimeMillis();
        String realIp = IpUtils.getRealIp(request);
        logEntity.setIp(realIp);

        String cityInfo = IpUtils.getCityInfo(realIp);
        logEntity.setIpSource(cityInfo);

        Map<String, String> osAndBrowserInfo = IpUtils.getOsAndBrowserInfo(request);
        // 操作系统
        logEntity.setPlatform(osAndBrowserInfo.get(OS));
        // 浏览器
        logEntity.setBrowser(osAndBrowserInfo.get(BROWSER));
        //获取执行时间
        Date day = new Date();
        logEntity.setCreateTime(day);
        //获取切入点属性
        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        //获取操作
        LogAnnotation myLog = method.getAnnotation(LogAnnotation.class);
        if (myLog != null) {
            String message = myLog.message();
            System.out.println("用户行为：" + message);
            logEntity.setUserBehavior(message);
            logEntity.setInterfaceName(message);
        }

        logEntity.setCreateName(String.valueOf(SecurityUtils.getUserId()));
        logEntity.setRequestTime(System.currentTimeMillis() - start);
        logService.save(logEntity);
    }
}
