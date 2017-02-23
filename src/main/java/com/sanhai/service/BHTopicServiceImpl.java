package com.sanhai.service;

import com.sanhai.dataSource.DataSource;
import com.sanhai.dataSource.TradingDataSource;
import com.sanhai.repository.BHTopicDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 作者：tianjiayuan
 * 创建时间：2017-02-23 9:41
 * 类描述：
 * 修改人：
 * 修改时间：
 */
@Service("bhTopicService")
public class BHTopicServiceImpl implements BHTopicService{

    @Resource
    private BHTopicDao bhTopicDao;

    @TradingDataSource(name = DataSource.A_SOURCE)
    public List<Map<String, Object>> loadTopics(Map<String, Object> params, int currPage, int pageSize){
        String sqlLimit = "limit " + currPage + "," + pageSize;
        return this.bhTopicDao.loadTopics(sqlLimit);
    }
}
