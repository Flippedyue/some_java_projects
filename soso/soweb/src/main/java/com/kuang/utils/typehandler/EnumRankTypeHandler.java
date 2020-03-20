package com.kuang.utils.typehandler;

import com.kuang.pojo.enums.BaseEnum;
import com.kuang.pojo.enums.Rank;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(value = {Rank.class})
public class EnumRankTypeHandler<E extends Enum<E> & BaseEnum> extends BaseEnumTypeHandler<E> {

    public EnumRankTypeHandler(Class<E> type) {
        super(type);
    }
}