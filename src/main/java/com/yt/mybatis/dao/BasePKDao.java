package com.yt.mybatis.dao;

import com.yt.commons.Page;
import com.yt.mybatis.model.BaseExample;
import com.yt.mybatis.model.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yt on 2017-7-6.
 */
public interface BasePKDao<PK extends Serializable, Model extends BaseModel, Example extends BaseExample> {
	Page<Model> queryOfPage(Example example);

	List<Model> selectPageByExample(Example example);

	long countByExample(Example example);

	int deleteByExample(Example example);

	int deleteByPrimaryKey(PK pk);

	int insert(Model record);

	int insertSelective(Model record);

	List<Model> selectByExample(Example example);

	Model selectByPrimaryKey(PK pk);

	int updateByExampleSelective(Model record, Example example);

	int updateByExample(Model record, Example example);

	int updateByPrimaryKeySelective(Model record);

	int updateByPrimaryKey(Model record);
}
