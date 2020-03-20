package com.kuang.service;

import com.kuang.pojo.ConsumInfo;

import java.util.List;

public interface ConsumInfoService {

    // 删除一个卡号的所有消费记录
    int delete(String cardNumber);

    // 添加一条消费记录
    int insert(ConsumInfo record);

    // 查询一个卡号所有的消费记录
    List<ConsumInfo> selectByCardNumber(String cardNumber);
}
