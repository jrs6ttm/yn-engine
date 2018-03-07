package com.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;

/**
 * 日志记录类封装
 * @author zhangLL
 *
 */
public class SysLog {
	    public static Logger log = Logger.getLogger(SysLog.class);
	    
	    /**
	     * 初始化日志信息
	     * @return 包名.类名.方法名(行数)
	     */
	    public static LogEntity initLogInfo(String logLevel){
            /*** 获取输出信息的代码的位置 ***/
            StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
            LogEntity logEntity = new LogEntity(logLevel, stacks[2].getClassName(), stacks[2].getMethodName(), stacks[2].getLineNumber(), null, null, null);
            
            return logEntity;
	    }
	    
	    /**
	     * 
	     * 处理日志信息
	     * 
	     * @param message
	     * @return
	     */
	    public static String getMessage(Object message){
            /*** 是否是异常  ***/
            if (message instanceof Exception) {
                Exception e = (Exception) message;
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw, true));
                String str = sw.toString();
                
                return str;
            } else {
            	return message.toString();
            }
	    }
	    
	    
	    /**
	     * 打印调试
	     * @param optUserId 操作人员id， 可空，为null
	     * @param params	方法的参数， 可空，为null
	     * @param message	日志信息
	     */
	    public static void debug(String optUserId, String params, Object message) {
            /*** 获取输出信息的代码的位置 ***/
            StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
            LogEntity logEntity = new LogEntity("debug", stacks[2].getClassName(), stacks[2].getMethodName(), stacks[2].getLineNumber(), params, optUserId, null);
            logEntity.setMessage(SysLog.getMessage(message));
            
            log.debug(logEntity.toJSON());
	    }

	    
	    /**
	     * 打印一般信息
	     * @param optUserId 操作人员id， 可空，为null
	     * @param params	方法的参数， 可空，为null
	     * @param message	日志信息
	     */
	    public static void info(String optUserId, String params, Object message) {
            /*** 获取输出信息的代码的位置 ***/
            StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
            LogEntity logEntity = new LogEntity("info", stacks[2].getClassName(), stacks[2].getMethodName(), stacks[2].getLineNumber(), params, optUserId, null);
            logEntity.setMessage(SysLog.getMessage(message));
            
            log.info(logEntity.toJSON());
	    }

	    
	    /**
	     * 打印警告信息
	     * @param optUserId 操作人员id， 可空，为null
	     * @param params	方法的参数， 可空，为null
	     * @param message	日志信息
	     */
	    public static void warn(String optUserId, String params, Object message) {
	        /*** 获取输出信息的代码的位置 ***/
	        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
	        LogEntity logEntity = new LogEntity("warn", stacks[2].getClassName(), stacks[2].getMethodName(), stacks[2].getLineNumber(), params, optUserId, null);
	        logEntity.setMessage(SysLog.getMessage(message));
	        
	        log.warn(logEntity.toJSON());
	    }
	    
	    
	    /**
	     * 打印错误信息
	     * @param optUserId 操作人员id， 可空，为null
	     * @param params	方法的参数， 可空，为null
	     * @param message	日志信息
	     */
	    public static void error(String optUserId, String params, Object message) {
            /*** 获取输出信息的代码的位置 ***/
            StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
            LogEntity logEntity = new LogEntity("error", stacks[2].getClassName(), stacks[2].getMethodName(), stacks[2].getLineNumber(), params, optUserId, null);
            logEntity.setMessage(SysLog.getMessage(message));
            
	    	log.error(logEntity.toJSON());
	    }
	    
	    /**
	     * 未使用
	     * 向数据库告警表中插入信息
	     * @param obj
	     */
	    public static void dbWarn(Object obj) {
            String printInfo = "";
            /*** 获取输出信息的代码的位置 ***/
            String location = "";
            StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
            location = stacks[2].getClassName() + "." + stacks[2].getMethodName()
                    + "[" + stacks[2].getLineNumber() + " 行]";
            
            /*** 是否是异常  ***/
            if (obj instanceof Exception) {
                Exception e = (Exception) obj;
                printInfo = location + e.getMessage();
                log.fatal(printInfo.substring(0, printInfo.length() > 512?512:printInfo.length()));
            } else {
                printInfo = location + obj.toString();
                log.fatal(printInfo.substring(0, printInfo.length() > 512?512:printInfo.length()));
            }
	    }
	    
	    public static String getCodeLocation1(){
            /*** 获取输出信息的代码的位置 ***/
            String location = "";
            StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
            location = stacks[2].getClassName() + "." + stacks[2].getMethodName()
                    + "[" + stacks[2].getLineNumber() + " 行]";
            
            return location;
	    }
}
