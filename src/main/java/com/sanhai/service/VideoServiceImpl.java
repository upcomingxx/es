package com.sanhai.service;

import com.sanhai.dataSource.DataSource;
import com.sanhai.dataSource.TradingDataSource;
import com.sanhai.repository.VideoDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 作者：boys
 * 创建时间：2017-02-23 9:45
 * 类描述：
 * 修改人：
 * 修改时间：
 */
@Service("videoService")
public class VideoServiceImpl implements VideoService {

    @Resource
    private VideoDao videoDao;

    @Override
    @TradingDataSource(name = DataSource.B_SOURCE)
    public List<Map<String, Object>> selectVideos() {
        return this.videoDao.selectVideo();
    }
}
