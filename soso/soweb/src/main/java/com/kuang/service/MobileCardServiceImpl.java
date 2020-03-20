package com.kuang.service;

import com.kuang.dao.MobileCardMapper;
import com.kuang.pojo.MobileCard;

import java.util.List;

public class MobileCardServiceImpl implements MobileCardService{

    //调用dao层的操作，设置一个set接口，方便Spring管理
    private MobileCardMapper mobileCardMapper;

    public void setMobileCardMapper (MobileCardMapper mobileCardMapper) {
        this.mobileCardMapper = mobileCardMapper;
    }

    public int insertCard (MobileCard card) {
        return mobileCardMapper.insertCard(card);
    }

    public MobileCard getMobileCard (String cardNumber) {
        return mobileCardMapper.getMobileCard(cardNumber);
    }

    public List<MobileCard> selectAll () {
        return mobileCardMapper.selectAll();
    }

    public int dupCount (List<String> NewNumbers) {
        return mobileCardMapper.dupCount(NewNumbers);
    }

    public int delete(String cardNumber) {
        return mobileCardMapper.delete(cardNumber);
    }

    public int update(MobileCard card) {
        return mobileCardMapper.update(card);
    }

    public int charge(double money, String cardNumber) {
        return mobileCardMapper.charge(money, cardNumber);
    }

}
