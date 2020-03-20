package com.kuang.service;

import com.kuang.pojo.MobileCard;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MobileCardService {

    // 增加用户
    int insertCard(MobileCard card);

    // 根据卡号查询用户
    MobileCard getMobileCard(String cardNumber);

    // 查询所有用户
    List<MobileCard> selectAll();

    // 查询重复个数
    int dupCount(List<String> NewNumbers);

    // 给用户状态设置为无效
    int delete(String cardNumber);

    // 更新用户信息
    int update(MobileCard card);

    // 根据用户卡号，充值一定金额的钱数
    int charge(double money, String cardNumber);
}
