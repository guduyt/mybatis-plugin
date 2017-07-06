package com.yt.mybatis.model;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * BaseMapper
 *
 * @author yitao
 * @version 1.0.0
 * @date 2016/8/22 9:14
 */
public interface BaseMapper<Model extends BaseModel,Example extends BaseExample> {

    /*分页查询使用*/
    List<Model> selectPageByExample(Example example);

	/*批量插入使用*/
    int insertBatch(List<Model> list);

    int countByExample(Example example);

    int deleteByExample(Example example);

    int insert(Model record);

    int insertSelective(Model record);

    List<Model> selectByExample(Example example);

    int updateByExampleSelective(@Param("record") Model record, @Param("example") Example example);

    int updateByExample(@Param("record") Model record, @Param("example") Example example);
}
