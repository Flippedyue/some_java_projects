package com.kuang.service;

import com.kuang.dao.ConsumInfoMapper;
import com.kuang.pojo.ConsumInfo;

import java.util.List;

public class ConsumInfoServiceImpl implements ConsumInfoService{

    private ConsumInfoMapper consumInfoMapper;

    public void setConsumInfoMapper(ConsumInfoMapper consumInfoMapper) {
        this.consumInfoMapper = consumInfoMapper;
    }

    public int delete(String cardNumber) {
        return consumInfoMapper.delete(cardNumber);
    }

    // 添加一条消费记录
    public int insert(ConsumInfo record) {
        return consumInfoMapper.insert(record);
    }

    // 查询一个卡号所有的消费记录
    public List<ConsumInfo> selectByCardNumber(String cardNumber) {
        return consumInfoMapper.selectByCardNumber(cardNumber);
    }

}
