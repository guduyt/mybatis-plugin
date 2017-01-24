package com.yt.mybatis.model;

import java.io.Serializable;

/**
 * Created by yt on 2017-1-24.
 */
public interface BasePKMapper <PK extends Serializable,Mode extends BaseModel,Example extends BaseExample> extends BaseMapper<Mode,Example> {

    int deleteByPrimaryKey(PK pk);

    Mode selectByPrimaryKey(PK pk);

    int updateByPrimaryKeySelective(Mode record);

    int updateByPrimaryKey(Mode record);
}
