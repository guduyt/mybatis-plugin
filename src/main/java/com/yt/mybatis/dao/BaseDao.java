package com.yt.mybatis.dao;

import com.yt.commons.Page;
import com.yt.mybatis.model.BaseExample;
import com.yt.mybatis.model.BaseModel;

import java.util.List;

/**
 * Created by yt on 2017-7-6.
 */
public interface BaseDao<Model extends BaseModel, Example extends BaseExample> {
	Page<Model> queryOfPage(Example example);

	List<Model> selectPageByExample(Example example);

	long countByExample(Example example);

	int deleteByExample(Example example);

	int insertBatch(List<Model> list);

	int insert(Model record);

	int insertSelective(Model record);

	List<Model> selectByExample(Example example);

	int updateByExampleSelective(Model record, Example example);

	int updateByExample(Model record, Example example);
}
