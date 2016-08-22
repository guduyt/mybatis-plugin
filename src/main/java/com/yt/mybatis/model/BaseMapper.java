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
    List<Mode> selectPageByExample(Example example);
}
