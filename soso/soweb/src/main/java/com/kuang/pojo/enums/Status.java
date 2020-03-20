package com.kuang.pojo.enums;

public enum Status implements BaseEnum<Rank, Integer> {
    Normal, Overdue, Out_of_service, ExitSoso,

    Status() { };

    @Override
    public Integer getValue() {
        return this.ordinal();
    }

}//正常  欠费  停机
