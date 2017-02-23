package com.sanhai.repository;


import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 作者：tianjiayuan
 * 创建时间：2016-09-14 14:21
 * 类描述：搜索服务的
 * 修改人：
 * 修改时间：
 */
public interface BHTopicDao {

    public List<Map<String, Object>> loadTopics(@Param("sqlLimit") String sqlLimit);
}
