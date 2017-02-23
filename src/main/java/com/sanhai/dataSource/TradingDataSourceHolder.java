package com.sanhai.dataSource;

/**
 * Created by zhangb on 2016/11/1.
 * 数据源处理类
 * 将获取到的数据源存放到线程副本中
 */
public class TradingDataSourceHolder {

	private static final ThreadLocal<String> threadDataSource = new ThreadLocal<String>();

	public static String getThreadDataSource() {
		String dataSourceName = (String) threadDataSource.get();
		if (dataSourceName == null || dataSourceName.length() < 1) {
			//设置默认写数据源
			dataSourceName = DataSource.A_SOURCE;
		}
		return dataSourceName;
	}

	public static void setThreadDataSource(String dataSourceName) {
		threadDataSource.set(dataSourceName);
	}

	public static void clearThreadDataSource() {
		threadDataSource.remove();
	}

}
