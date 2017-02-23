package com.sanhai.dataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;

/**
 * Created by zhangb on 2016/11/1.
 * 动态数据源的子类
 * 重写get数据源方法
 */
public class TradingRoutingDataSource extends AbstractRoutingDataSource {
	
	@Override
	protected Object determineCurrentLookupKey() {
		return TradingDataSourceHolder.getThreadDataSource();
	}
	
	@Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {  
        super.setTargetDataSources(targetDataSources);  
        afterPropertiesSet();
    }  
	 

}
