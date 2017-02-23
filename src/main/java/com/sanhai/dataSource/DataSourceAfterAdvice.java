package com.sanhai.dataSource;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.Method;

/**
 * Created by zhangb on 2016/11/1.
 * 数据源aop后续通知
 * 执行完方法释放线程副本变量的值
 */
public class DataSourceAfterAdvice implements AfterReturningAdvice, ThrowsAdvice {


	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		TradingDataSourceHolder.clearThreadDataSource();
	}

	public void afterThrowing(Exception ex) {
		TradingDataSourceHolder.clearThreadDataSource();
	}

}
