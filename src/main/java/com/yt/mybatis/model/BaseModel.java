package com.yt.mybatis.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * BaseModel
 *
 * @author yitao
 * @version 1.0.0
 * @date 2016/8/19 12:57
 */
public abstract class BaseModel {
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
