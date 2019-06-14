package com.bbjh.admin.mapping;

import com.bbjh.admin.api.admin.sys.RightAddRequest;
import com.bbjh.admin.api.admin.sys.RightModifyRequest;
import com.bbjh.common.model.Right;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RightMapping {

    Right convert(RightAddRequest aoRightAddRequest);

    Right convert(RightModifyRequest aoRightAddRequest);
}
