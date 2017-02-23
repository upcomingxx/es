package com.sanhai.service;

import java.util.List;
import java.util.Map;

/**
 * 作者：tianjiayuan
 * 创建时间：2017-02-23 9:41
 * 类描述：
 * 修改人：
 * 修改时间：
 */
public interface BHTopicService {

    public List<Map<String, Object>> loadTopics(Map<String, Object> params, int currPage, int pageSize);
}
