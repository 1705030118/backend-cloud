package com.ldm.gateway.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * AnnotationLogAspect: 注解式日志切面
 */
@Aspect
@Component
public class AnnotationLogAspect {
    private static Logger logger = LogManager.getLogger(AnnotationLogAspect.class.getName());
    /**
     * 声明切点，使用了注解 @Action 的方法会拦截生效
     */
    @Pointcut("@annotation(com.ldm.gateway.aop.Action)")
    public void annotationPointcut() {
    }

    /**
     * 声明前置建言，复用了@Pointcut注解定义的切点
     */
    @Before("annotationPointcut()")
    public void before(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Action action = method.getAnnotation(Action.class); //获取方法上定义的注解，粒度更细更精确
        System.out.println("注解式拦截，即将执行：" + action.name()); // 反射获得注解上的属性，然后做日志相关的记录操作
    }

    /**
     * 声明后继建言，复用了@Pointcut注解定义的切点
     */
    @After("annotationPointcut()")
    public void after(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Action action = method.getAnnotation(Action.class);
        System.out.println("注解式拦截，刚刚执行完：" + action.name());
    }

    /**
     * 异常通知 记录操作报错日志
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "annotationPointcut()",throwing = "e")
    public void AfterThrowing(JoinPoint joinPoint, Throwable e){
        logger.error("happened error");
    }
}
