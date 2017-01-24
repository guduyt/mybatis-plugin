package com.yt.mybatis.model;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * BaseMapper
 *
 * @author yitao
 * @version 1.0.0
 * @date 2016/8/22 9:14
 */
public interface BaseMapper<Mode extends BaseModel,Example extends BaseExample> {

    /*分页查询使用*/
    List<Mode> selectPageByExample(Example example);

	/*批量插入使用*/
    int insertBatch(List<Mode> list);

    int countByExample(Example example);

    int deleteByExample(Example example);

    int insert(Mode record);

    int insertSelective(Mode record);

    List<Mode> selectByExample(Example example);

    int updateByExampleSelective(@Param("record") Mode record, @Param("example") Example example);

    int updateByExample(@Param("record") Mode record, @Param("example") Example example);
}
