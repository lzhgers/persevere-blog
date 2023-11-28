package com.lzh.lzhblog.aspect;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lzh.lzhblog.annotation.LogAnnotation;
import com.lzh.lzhframework.domain.entity.ExceptionLogEntity;
import com.lzh.lzhframework.domain.entity.LogEntity;
import com.lzh.lzhframework.domain.entity.LoginUser;
import com.lzh.lzhframework.domain.entity.User;
import com.lzh.lzhframework.holder.RequestHolder;
import com.lzh.lzhframework.service.ExceptionLogService;
import com.lzh.lzhframework.service.LogService;
import com.lzh.lzhframework.service.impl.SearcherService;
import com.lzh.lzhframework.utils.IpUtil;
import com.lzh.lzhframework.utils.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

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

    @Resource
    private SearcherService searcherService;

    @Resource
    private ExceptionLogService exceptionLogService;

    private static Logger LOGGER = LoggerFactory.getLogger(SystemLogAspect.class);

    /**
     * 定义切点@PointCut
     * 在注解位置切入代码
     */
    @Pointcut("@annotation(com.lzh.lzhblog.annotation.LogAnnotation)")
    public void logPointCut() {
    }

//    //前置通知
//    //在执行方法之前打印获取的参数内容
//    @Before("logPointCut()")
//    public void before(JoinPoint joinPoint) throws UnsupportedEncodingException {
//        HttpServletRequest request = RequestHolder.getRequest();
//
//        assert request != null;
//        request.setCharacterEncoding("UTF-8");
//
//        LOGGER.info("URL: {}", request.getRequestURL().toString());
//        LOGGER.info("HTTP请求类型: {}", request.getMethod());
//        LOGGER.info("执行方法: {}", joinPoint);
//        LOGGER.info("传递参数: {}", Arrays.toString(joinPoint.getArgs()));
//        LOGGER.info("IP地址:   {}: ", request.getRemoteAddr());
//
//        LogEntity logEntity = new LogEntity();
//        String url = request.getRequestURL().toString();
//        url = url.substring(21);
//        System.out.println("请求接口：" + url);
//        logEntity.setRequestInterface(url);
//
//        String requestMethod = request.getMethod();
//        System.out.println("获取请求方式" + requestMethod);
//        logEntity.setRequestWay(requestMethod);
//
//        String requestParams = Arrays.toString(joinPoint.getArgs());
//        logEntity.setContent(requestParams);
//        System.out.println("获取请求参数" + requestParams);
//
//        //开始调用时间
//        // 计时并调用目标函数
//        long start = System.currentTimeMillis();
//        String ipAddr = IpUtil.getIpAddress(request);
//        logEntity.setIp(ipAddr);
//
//        String region = searcherService.getRegion(ipAddr);
//        logEntity.setIpSource(region);
//
//        Map<String, String> osAndBrowserInfo = IpUtil.getOsAndBrowserInfo(request);
//        // 操作系统
//        logEntity.setPlatform(osAndBrowserInfo.get(OS));
//        // 浏览器
//        logEntity.setBrowser(osAndBrowserInfo.get(BROWSER));
//        //获取执行时间
//        Date day = new Date();
//        logEntity.setCreateTime(day);
//        //获取切入点属性
//        //从切面织入点处通过反射机制获取织入点处的方法
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        //获取切入点所在的方法
//        Method method = signature.getMethod();
//        //获取操作
//        LogAnnotation myLog = method.getAnnotation(LogAnnotation.class);
//        if (myLog != null) {
//            String message = myLog.message();
//            System.out.println("用户行为：" + message);
//            logEntity.setUserBehavior(message);
//            logEntity.setInterfaceName(message);
//        }
//
//        LoginUser loginUser = SecurityUtils.getLoginUser();
//        if (loginUser != null) {
//            User user = loginUser.getUser();
//            if (user != null) {
//                logEntity.setCreateName(user.getUserName());
//                logEntity.setOperatePerson(user.getUserName());
//            }
//        }
//        logEntity.setRequestTime(System.currentTimeMillis() - start);
//        logService.save(logEntity);
//    }

    //前置通知
    //在执行方法之前打印获取的参数内容
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = RequestHolder.getRequest();

        assert request != null;
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
        String ipAddr = IpUtil.getIpAddress(request);
        logEntity.setIp(ipAddr);

        String region = searcherService.getRegion(ipAddr);
        logEntity.setIpSource(region);

        Map<String, String> osAndBrowserInfo = IpUtil.getOsAndBrowserInfo(request);
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

        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null) {
            User user = loginUser.getUser();
            if (user != null) {
                logEntity.setCreateName(user.getUserName());
                logEntity.setOperatePerson(user.getUserName());
            }
        }
        Object proceed = joinPoint.proceed();
        logEntity.setRequestTime(System.currentTimeMillis() - start);
        logService.save(logEntity);
        return proceed;
    }

    @Pointcut("execution(* com.lzh.lzhblog.controller..*.*(..))")
    public void expPointcut() {
    }

    @AfterThrowing(value = "expPointcut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        ExceptionLogEntity exception = new ExceptionLogEntity();
        HttpServletRequest request = RequestHolder.getRequest();
        String ip = IpUtil.getIpAddress(request);
        exception.setIp(ip);

        //从Redis中获取IP来源
        String region = searcherService.getRegion(ip);
        exception.setIpSource(region);

        String requestParams = Arrays.toString(joinPoint.getArgs());
        exception.setParams(requestParams);

        //设置调用的方法
        assert request != null;
        exception.setMethod(request.getRequestURI());

        exception.setExceptionJson(JSON.toJSONString(e,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue));
        exception.setExceptionMessage(e.getMessage());

        exception.setCreateTime(new Date());
        exception.setCreateName(SecurityUtils.getUsername());

        exceptionLogService.save(exception);
    }

}
