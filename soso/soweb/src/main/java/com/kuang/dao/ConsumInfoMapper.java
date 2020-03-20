package com.kuang.dao;

import com.kuang.pojo.ConsumInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface ConsumInfoMapper {
    @Update({
            "update soso_consuminfo",
            "set tag = 0",
            "where cardNumber = #{cardNumber,jdbcType=CHAR}"
    })
    int delete(String cardNumber);

    @Insert({
            "insert into soso_consuminfo (cardNumber, ",
            "type, consumData, tag)",
            "values (#{cardNumber,jdbcType=CHAR}, ",
            "#{type,jdbcType=CHAR}, #{consumData,jdbcType=INTEGER} ,#{tag,jdbcType=INTEGER})"
    })
    int insert(ConsumInfo record);

    @Select({
            "select",
            "id, cardNumber, type, consumData, tag",
            "from soso_consuminfo",
            "where cardNumber = #{cardNumber,jdbcType=CHAR} and tag=1"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "cardNumber", property = "cardNumber", jdbcType = JdbcType.CHAR),
            @Result(column = "type", property = "type", jdbcType = JdbcType.CHAR),
            @Result(column = "consumData", property = "consumData", jdbcType = JdbcType.INTEGER),
            @Result(column = "tag", property = "tag", jdbcType = JdbcType.INTEGER)
    })
    List<ConsumInfo> selectByCardNumber(String cardNumber);
}