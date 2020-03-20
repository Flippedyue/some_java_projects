package com.kuang.utils.typehandler;

import com.kuang.pojo.enums.BaseEnum;
import com.kuang.pojo.enums.Status;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(value = {Status.class})
public class EnumStatusTypeHandler<E extends Enum<E> & BaseEnum> extends BaseEnumTypeHandler<E> {

    public EnumStatusTypeHandler(Class<E> type) {
        super(type);
    }
}
