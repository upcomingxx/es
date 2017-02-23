package com.sanhai.repository;

import java.util.List;
import java.util.Map;

/**
 * 作者：boys
 * 创建时间：2017-02-23 17:34
 * 类描述：
 * 修改人：
 * 修改时间：
 */
public interface VideoDao {

    public List<Map<String, Object>> selectVideo();
}
