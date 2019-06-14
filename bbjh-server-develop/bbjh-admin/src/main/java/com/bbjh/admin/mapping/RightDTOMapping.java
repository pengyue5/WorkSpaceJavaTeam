package com.bbjh.admin.mapping;

import com.bbjh.common.dto.RightInfoDTO;
import com.bbjh.common.model.Right;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RightDTOMapping {

    @Mappings({
        @Mapping(source = "rightName", target = "rightName", ignore = true),
        @Mapping(source = "children", target = "children", ignore = true)
    })
    RightInfoDTO convert(Right right);
}
