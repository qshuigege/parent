package com.example.demo.utils;

import ch.qos.logback.classic.LoggerContext;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ChangeLogLevelUtils {

    public enum LoggerType{
        LOG4J , LOGBACK
    }
    public enum Level {
        TRACE, DEBUG, INFO, WARN, ERROR
    }

    public static void setLevel(LoggerType loggerImplType, Level level) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        if (loggerImplType==LoggerType.LOGBACK){
            ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();//得到的实际对象是"ch.qos.logback.classic.LoggerContext"
            Class<? extends ILoggerFactory> clazz = iLoggerFactory.getClass();
            Method getLogger = clazz.getDeclaredMethod("getLogger", String.class);
            Object root = getLogger.invoke(iLoggerFactory, "ROOT");//root对象类型为ch.qos.logback.classic.Logger
            Class<?> rootClazz = root.getClass();
            Class<?> levelClazz = Class.forName("ch.qos.logback.classic.Level");
            Constructor<?> levelClazzConstructor = levelClazz.getConstructor(int.class, String.class);
            Object levelObj;
            if (level==Level.TRACE) {
                levelObj = levelClazzConstructor.newInstance(5000, "TRACE");
            }else if (level == Level.DEBUG){
                levelObj = levelClazzConstructor.newInstance(10000, "DEBUG");
            }else if (level == Level.INFO){
                levelObj = levelClazzConstructor.newInstance(20000, "INFO");
            }else if (level == Level.WARN){
                levelObj = levelClazzConstructor.newInstance(30000, "WARN");
            }else if (level == Level.ERROR){
                levelObj = levelClazzConstructor.newInstance(40000, "ERROR");
            }else {
                return;
            }
            Method setLevel = rootClazz.getDeclaredMethod("setLevel", levelClazz);
            setLevel.invoke(root, levelObj);
        }else if (loggerImplType == LoggerType.LOG4J){
            //获得rootLogger
            Class<?> logManagerClazz = Class.forName("org.apache.log4j.LogManager");
            Method getRootLoggerMethod = logManagerClazz.getDeclaredMethod("getRootLogger");
            Object rootLoggerObj = getRootLoggerMethod.invoke(null);//得到的实际对象是org.apache.log4j.Logger
            Class<?> rootLoggerClazz = rootLoggerObj.getClass();

            //获得rootLogger对象的setLevel方法的参数对象
            Class<?> levelClazz = Class.forName("org.apache.log4j.Level");
            Constructor<?> levelClazzConstructor = levelClazz.getDeclaredConstructor(int.class, String.class, int.class);
            levelClazzConstructor.setAccessible(true);
            Object levelObj;


            Method getAppenderMethod = rootLoggerClazz.getMethod("getAppender", String.class);
            Object consoleAppenderObj = getAppenderMethod.invoke(rootLoggerObj, "service");//得到的实际对象是org.apache.log4j.ConsoleAppender
            Class<?> consoleAppenderClazz = consoleAppenderObj.getClass();
            Class<?> priorityClazz = Class.forName("org.apache.log4j.Priority");
            Field levelField;


            if (level==Level.TRACE) {
                levelObj = levelClazzConstructor.newInstance(5000, "TRACE", 7);
                levelField= priorityClazz.getDeclaredField("DEBUG");
            }else if (level == Level.DEBUG){
                levelObj = levelClazzConstructor.newInstance(10000, "DEBUG", 7);
                levelField= priorityClazz.getDeclaredField("DEBUG");
            }else if (level == Level.INFO){
                levelObj = levelClazzConstructor.newInstance(20000, "INFO", 6);
                levelField= priorityClazz.getDeclaredField("INFO");
            }else if (level == Level.WARN){
                levelObj = levelClazzConstructor.newInstance(30000, "WARN", 4);
                levelField= priorityClazz.getDeclaredField("WARN");
            }else if (level == Level.ERROR){
                levelObj = levelClazzConstructor.newInstance(40000, "ERROR", 3);
                levelField= priorityClazz.getDeclaredField("ERROR");
            }else {
                return;
            }
            Method setLevelMethod = rootLoggerClazz.getDeclaredMethod("setLevel", levelClazz);
            //设置rootLogger的输出级别
            setLevelMethod.invoke(rootLoggerObj, levelObj);
            Object priorityInstance = levelField.get(null);
            Method setThresholdMethod = consoleAppenderClazz.getMethod("setThreshold", priorityClazz);
            //设置Appender的输出级别
            setThresholdMethod.invoke(consoleAppenderObj, priorityInstance);


        }
    }

}
