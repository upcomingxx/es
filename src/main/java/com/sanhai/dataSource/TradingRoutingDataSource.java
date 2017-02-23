package com.sanhai.dataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;

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
