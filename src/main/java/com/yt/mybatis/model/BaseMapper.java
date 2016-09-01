package com.yt.mybatis.model;

import java.io.Serializable;
import java.util.List;

/**
 * BaseMapper
 *
 * @author yitao
 * @version 1.0.0
 * @date 2016/8/22 9:14
 */
public interface BaseMapper<Pk extends Serializable,Mode extends BaseModel,Example extends BaseExample> {

    /*分页查询使用*/
    List<Mode> selectPageByExample(Example example);

    /*单条插入使用*/
    int insert(Mode record);

	/*批量插入使用*/
    int insertBatch(List<Mode> list);
}
