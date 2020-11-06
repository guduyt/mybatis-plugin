package com.yt.mybatis.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.yt.commons.Page;
import com.yt.mybatis.model.BaseExample;
import com.yt.mybatis.model.BaseMapper;
import com.yt.mybatis.model.BaseModel;

/**
 * Created by yt on 2017-7-6.
 */
public abstract class BaseDaoAdapter<Model extends BaseModel, Example extends BaseExample, Mapper extends BaseMapper<Model, Example>> implements BaseDao<Model,Example>{

	@Autowired
	protected Mapper mapper;

	protected Mapper getMapper() {
		return mapper;
	}

	public Page<Model> queryOfPage(Example example) {
		long cnt = getMapper().countByExample(example);

		Page<Model> page = new Page<>();
		page.setCurrentPage(example.getCurrentPage());
		page.setPageSize(example.getPageSize());
		page.setTotalRow(cnt);
		// auto set totalPage,startRow,endRow;

		if (cnt > 0) {
			List<Model> list = getMapper().selectPageByExample(example);
			page.setData(list);
		}
		return page;
	}

	@Override
	public List<Model> selectPageByExample(Example example) {
		return getMapper().selectPageByExample(example);
	}

	@Override
	public long countByExample(Example example) {
		return getMapper().countByExample(example);
	}

	@Override
	public int deleteByExample(Example example) {
		return getMapper().deleteByExample(example);
	}

	@Override
	public int insertBatch(List<Model> list){
		//控制批量插入数据的大小，不执行一次超过1000条数据的批量插入操作
		if(null!=list && list.size()<=1000)
			return getMapper().insertBatch(list);
		return -1;
	}

	@Override
	public int insert(Model record) {
		return getMapper().insertSelective(record);
	}

	@Override
	public int insertSelective(Model record) {
		return getMapper().insertSelective(record);
	}

	@Override
	public List<Model> selectByExample(Example example) {
		return getMapper().selectByExample(example);
	}

	@Override
	public int updateByExampleSelective(Model record, Example example) {
		return getMapper().updateByExampleSelective(record, example);
	}

	@Override
	public int updateByExample(Model record, Example example) {
		return getMapper().updateByExample(record, example);
	}

}
