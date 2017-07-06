package com.yt.mybatis.model;

import java.io.Serializable;

/**
 * Created by yt on 2017-1-24.
 */
public interface BasePKMapper <PK extends Serializable,Model extends BaseModel,Example extends BaseExample> extends BaseMapper<Model,Example> {

    int deleteByPrimaryKey(PK pk);

    Model selectByPrimaryKey(PK pk);

    int updateByPrimaryKeySelective(Model record);

    int updateByPrimaryKey(Model record);
}
