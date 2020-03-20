package com.kuang.dao;

import com.kuang.pojo.Scene;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface SceneMapper {
    @Select({
            "select",
            "id, type, data, description",
            "from soso_scene",
            "where type = #{type,jdbcType=CHAR}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.INTEGER, id=true),
            @Result(column="type", property="type", jdbcType=JdbcType.CHAR),
            @Result(column="data", property="data", jdbcType=JdbcType.INTEGER),
            @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR)
    })
    List<Scene> selectByType(String type);

    @Select({
            "select",
            "id, type, data, description",
            "from soso_scene"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="type", property="type", jdbcType=JdbcType.CHAR),
            @Result(column="data", property="data", jdbcType=JdbcType.INTEGER),
            @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR)
    })
    List<Scene> selectAll();
}
