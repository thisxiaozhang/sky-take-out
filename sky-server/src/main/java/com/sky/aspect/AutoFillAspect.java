package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author
 * @version 1.0
 * @description: 自定义填充公共字段的切面
 * @date 2024/4/7 20:55
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {

    }

    /**
     * 前置通知，在通知中进行公共字段的赋值
     */
    @Before("autoFillPointCut()")
    public void AutoFill(JoinPoint joinPoint) {
        log.info("开启公共字段自动填充....");
        //获取方法签名对象
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取方法上的注解对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);

        OperationType operationType = autoFill.value();//获取操作类型
        //获取被拦截方法的参数对象
        Object[] args = joinPoint.getArgs();
        log.info("获取到被拦截方法的参数对象:{}", args);
        if (args == null || args.length == 0) {
            log.info("没有参数，无法完成公共字段的填充");
            return;
        }
        Object entity = args[0];//获取第一个参数，即实体对象

        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        switch (operationType) {
            case INSERT:
                //为四个公共字段赋值
                try {
                    Method setCreateTime = entity.getClass().
                            getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                    Method setUpdateTime = entity.getClass().
                            getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                    Method setCreateUser = entity.getClass().
                            getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                    Method setUpdateUser = entity.getClass().
                            getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                    setCreateTime.invoke(entity, now);
                    setUpdateTime.invoke(entity, now);
                    setCreateUser.invoke(entity, currentId);
                    setUpdateUser.invoke(entity, currentId);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                break;
            case UPDATE:
                try {
                    Method setUpdateTime = entity.getClass().
                            getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                    Method setUpdateUser = entity.getClass().
                            getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                    setUpdateTime.invoke(entity, now);
                    setUpdateUser.invoke(entity, currentId);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                break;
        }

    }

}
