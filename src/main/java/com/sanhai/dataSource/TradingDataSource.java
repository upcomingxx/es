package com.sanhai.dataSource;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface TradingDataSource {
	String name();
}
