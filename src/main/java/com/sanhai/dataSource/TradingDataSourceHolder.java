package com.sanhai.dataSource;

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
