package com.sanhai.dataSource;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * Created by zhangb on 2016/11/1.
 * 数据源aop前置通知
 * 执行完方法前用反射去类或方法上获取注解的值及数据源值
 */
public class DataSourceBeforeAdvice implements MethodBeforeAdvice {

	public void before(Method method, Object[] args, Object target) throws Throwable {
		TradingDataSource tds = null;
		tds = target.getClass().getAnnotation(TradingDataSource.class);
		if (tds == null) {
			tds = method.getAnnotation(TradingDataSource.class);
		}
		if (tds != null) {
			TradingDataSourceHolder.setThreadDataSource(tds.name());
		}
	}

}
