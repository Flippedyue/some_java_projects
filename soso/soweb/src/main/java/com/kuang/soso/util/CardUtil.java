package com.kuang.soso.util;

import com.kuang.pojo.MobileCard;
import com.kuang.pojo.enums.Rank;
import com.kuang.soso.abs.ServicePackage;
import com.kuang.soso.packages.NetPackage;
import com.kuang.soso.packages.SuperPackage;
import com.kuang.soso.packages.TalkPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class CardUtil {
    private static final String ROOT_ADDR = "/Users/jy/IdeaProjects/soso_mobile/";
    private static final String TALK_ADDR = "message/TalkPackage.txt";
    private static final String NET_ADDR = "message/NetPackage.txt";
    private static final String SUPER_ADDR = "message/SuperPackage.txt";
    private static final String FEE_ADDR = "message/Fee.txt";

    public static void showPackage(int Choose) throws IOException {
        String fileName = ROOT_ADDR;
        switch (Choose) {
            case 1:
                fileName += TALK_ADDR;
                break;
            case 2:
                fileName += NET_ADDR;
                break;
            case 3:
                fileName += SUPER_ADDR;
                break;
            case 4:
                fileName += FEE_ADDR;
                break;
            default:
                fileName = null;
        }
        Reader file = new FileReader(fileName);
        BufferedReader reader = new BufferedReader(file);
        String line = null;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        file.close();
        reader.close();
    }

    public static ServicePackage addPackage (int Choose) {
        ServicePackage Package = null;
        switch (Choose) {
            case 1:
                Package = new TalkPackage();
                break;
            case 2:
                Package = new NetPackage();
                break;
            case 3:
                Package = new SuperPackage();
                break;
            default:
                break;
        }
        return Package;
    }

    public static void showDescription() throws IOException {
        System.out.println("*****资费说明*****");
        for (int i = 1; i <= 3; i++) {
            showPackage(i);
            System.out.println("-----------------------------");
        }
        showPackage(4);
    }

    public static int GBtoMB(int flow) {
        return flow * 1024;
    }

    public static void changeRank(MobileCard card) {
        Rank rank = card.getRank();
        Rank newRank = rank;
        if ((card.getMoney() >= 0) && card.getConsumAmount() >= 200) {
            switch (rank) {
                case common:
                    newRank = Rank.silver;
                    break;
                case silver:
                    newRank = Rank.gold;
                    break;
                case gold:
                    newRank = Rank.diamond;
            }
        }
        card.setRank(newRank);
    }
}
