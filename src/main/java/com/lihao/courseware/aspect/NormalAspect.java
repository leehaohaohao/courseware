package com.lihao.courseware.aspect;


import com.lihao.courseware.annotation.Login;
import com.lihao.courseware.annotation.Teacher;
import com.lihao.courseware.exception.GlobalException;
import com.lihao.courseware.util.CheckTools;
import jakarta.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 *
 * @author lihao
 */
@Component
@Aspect
public class NormalAspect {
    @Resource
    private CheckTools checkTools;
    @Before("@annotation(com.lihao.courseware.annotation.Login)")
    public void login(JoinPoint joinPoint) throws GlobalException {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Login login = method.getAnnotation(Login.class);
        if(login ==null){
            return;
        }
        if(login.logging()){
            checkTools.checkLogin();
        }
    }
    @Before("@annotation(com.lihao.courseware.annotation.Teacher)")
    public void teacher(JoinPoint joinPoint) throws GlobalException {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Teacher teacher = method.getAnnotation(Teacher.class);
        if(teacher ==null){
            return;
        }
        if(teacher.isTeacher()){
            checkTools.checkTeacher();
        }
    }
    @Around("@annotation(com.lihao.courseware.annotation.Time)")
    public Object time(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        // 记录开始时间
        Object proceed = joinPoint.proceed();
        // 执行目标方法
        long endTime = System.currentTimeMillis();
        // 记录结束时间
        long executionTime = endTime - startTime;
        // 计算响应时间

        String logMessage = joinPoint.getSignature() + " 用时" + executionTime + "ms";
        System.out.println(logMessage);

        /*// 将日志写入文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\IDEProject\\courseware\\src\\main\\resources\\static\\time.log", true))) {
            writer.write(logMessage);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return proceed;
    }

}
