package com.kuang.dao;

import com.kuang.pojo.MobileCard;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface MobileCardMapper {
    @Insert({
            "insert into soso_mobilecard (cardNumber, userName, ",
            "passWord, realTalkTime, ",
            "realSMSCount, realFlow, ",
            "consumAmount, money, ",
            "realAmount, serPackage, ",
            "rank, ",
            "status)",
            "values (#{cardNumber,jdbcType=VARCHAR}, #{userName,jdbcType=VARCHAR}, ",
            "#{passWord,jdbcType=VARCHAR}, #{realTalkTime,jdbcType=INTEGER}, ",
            "#{realSMSCount,jdbcType=INTEGER}, #{realFlow,jdbcType=INTEGER}, ",
            "#{consumAmount,jdbcType=DOUBLE}, #{money,jdbcType=DOUBLE}, ",
            "#{realAmount,jdbcType=DOUBLE}, #{serPackage,jdbcType=INTEGER}, ",
            "#{rank,jdbcType=INTEGER}, ",
            "#{status,jdbcType=INTEGER})"
    })
    int insertCard(MobileCard card);

    @Select({
            "select",
            "cardNumber, userName, passWord, realTalkTime, realSMSCount, realFlow, consumAmount, ",
            "money, realAmount, serPackage, rank, status",
            "from soso_mobilecard",
            "where cardNumber = #{cardNumber,jdbcType=VARCHAR}",
            "and status <> 3"
    })
    @Results({
            @Result(column="cardNumber", property="cardNumber", jdbcType= JdbcType.VARCHAR, id=true),
            @Result(column="userName", property="userName", jdbcType=JdbcType.VARCHAR),
            @Result(column="passWord", property="passWord", jdbcType=JdbcType.VARCHAR),
            @Result(column="realTalkTime", property="realTalkTime", jdbcType=JdbcType.INTEGER),
            @Result(column="realSMSCount", property="realSMSCount", jdbcType=JdbcType.INTEGER),
            @Result(column="realFlow", property="realFlow", jdbcType=JdbcType.INTEGER),
            @Result(column="consumAmount", property="consumAmount", jdbcType=JdbcType.DOUBLE),
            @Result(column="money", property="money", jdbcType=JdbcType.DOUBLE),
            @Result(column="realAmount", property="realAmount", jdbcType=JdbcType.DOUBLE),
            @Result(column="serPackage", property="serPackage", jdbcType=JdbcType.INTEGER),
            @Result(column="rank", property="rank", jdbcType=JdbcType.INTEGER),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER)
    })
    MobileCard getMobileCard(String cardNumber);

    @Select({
            "select",
            "cardNumber, userName, passWord, realTalkTime, realSMSCount, realFlow, consumAmount, ",
            "money, realAmount, serPackage, rank, status",
            "from soso_mobilecard"
    })
    @Results({
            @Result(column="cardNumber", property="cardNumber", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="userName", property="userName", jdbcType=JdbcType.VARCHAR),
            @Result(column="passWord", property="passWord", jdbcType=JdbcType.VARCHAR),
            @Result(column="realTalkTime", property="realTalkTime", jdbcType=JdbcType.INTEGER),
            @Result(column="realSMSCount", property="realSMSCount", jdbcType=JdbcType.INTEGER),
            @Result(column="realFlow", property="realFlow", jdbcType=JdbcType.INTEGER),
            @Result(column="consumAmount", property="consumAmount", jdbcType=JdbcType.DOUBLE),
            @Result(column="money", property="money", jdbcType=JdbcType.DOUBLE),
            @Result(column="realAmount", property="realAmount", jdbcType=JdbcType.DOUBLE),
            @Result(column="serPackage", property="serPackage", jdbcType=JdbcType.INTEGER),
            @Result(column="rank", property="rank", jdbcType=JdbcType.INTEGER),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER)

    })
    List<MobileCard> selectAll();

    @Select({
            "<script>",
            "select count(*) from soso_mobilecard where cardNumber in ",
            "<foreach item='item' index='index' collection='NewNumbers' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"
    })
    int dupCount(@Param("NewNumbers") List<String> NewNumbers);


    @Update({
            "update soso_mobilecard",
            "set status = 3",
            "where cardNumber = #{cardNumber,jdbcType=VARCHAR}"
    })
    int delete(String cardNumber);

    @Update({
            "update soso_mobilecard",
            "set userName = #{userName,jdbcType=VARCHAR},",
            "passWord = #{passWord,jdbcType=VARCHAR},",
            "realTalkTime = #{realTalkTime,jdbcType=INTEGER},",
            "realSMSCount = #{realSMSCount,jdbcType=INTEGER},",
            "realFlow = #{realFlow,jdbcType=INTEGER},",
            "consumAmount = #{consumAmount,jdbcType=DOUBLE},",
            "money = #{money,jdbcType=DOUBLE},",
            "realAmount = #{realAmount,jdbcType=DOUBLE},",
            "rank = #{rank,jdbcType=INTEGER},",
            "status = #{status,jdbcType=INTEGER},",
            "serPackage = #{serPackage,jdbcType=INTEGER}",
            "where cardNumber = #{cardNumber,jdbcType=VARCHAR}"
    })
    int update(MobileCard card);

    @Update({
            "update soso_mobilecard",
            "set money = #{money,jdbcType=DOUBLE} + money",
            "where cardNumber = #{cardNumber,jdbcType=VARCHAR}"
    })
    int charge(@Param("money") double money, @Param("cardNumber") String cardNumber);
}
