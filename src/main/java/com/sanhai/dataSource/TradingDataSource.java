package com.sanhai.dataSource;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zhangb on 2016/11/1.
 * 数据源注解
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TradingDataSource {
	String name();
}
