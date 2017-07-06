package com.yt.mybatis.dao;

import java.io.Serializable;

import com.yt.mybatis.model.BaseExample;
import com.yt.mybatis.model.BaseModel;
import com.yt.mybatis.model.BasePKMapper;

/**
 * Created by yt on 2017-7-6.
 */
public abstract class BasePKDaoAdapter<PK extends Serializable, Model extends BaseModel, Example extends BaseExample, Mapper extends BasePKMapper<PK, Model, Example>> extends BaseDaoAdapter<Model, Example,Mapper> implements BasePKDao<PK, Model, Example> {

	@Override
	public int deleteByPrimaryKey(PK pk) {
		return getMapper().deleteByPrimaryKey(pk);
	}

	@Override
	public Model selectByPrimaryKey(PK pk) {
		return getMapper().selectByPrimaryKey(pk);
	}

	@Override
	public int updateByPrimaryKeySelective(Model record) {
		return getMapper().updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Model record) {
		return getMapper().updateByPrimaryKey(record);
	}
}
