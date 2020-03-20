package com.kuang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumInfo {
    static int IN_USE = 1;
    static int NO_USE = 0;

    private int id;
    private String cardNumber;
    private String type;
    private int consumData;
    private int tag;

    public ConsumInfo(String number, String type, int num, int tag) {
        this.cardNumber = number;
        this.type = type;
        this.consumData = num;
        this.tag = tag;
    }

    public String getUnit() {
        if ("通话".equals(getType())) {
            return "分钟";
        } else if ("短信".equals(getType())) {
            return "条";
        } else if ("上网".equals(getType())) {
            return "MB";
        }
        return "" ;
    }
}
