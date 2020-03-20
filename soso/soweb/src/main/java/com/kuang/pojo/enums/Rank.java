package com.kuang.pojo.enums;

public enum Rank implements BaseEnum<Rank, Integer> {
    common("普通卡", 0),
    silver("银卡", 50),
    gold("金卡", 100),
    diamond("钻石卡", 150);

    public String name;
    public int amount;

    Rank(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    @Override
    public Integer getValue() {
        return this.ordinal();
    }
}
