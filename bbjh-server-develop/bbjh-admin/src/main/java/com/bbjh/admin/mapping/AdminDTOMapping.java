package com.bbjh.admin.mapping;

import com.bbjh.admin.dto.AdminDTO;
import com.bbjh.common.model.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminDTOMapping {

    @Mapping(source = "rights", target = "rights", ignore = true)
    AdminDTO convert(Admin admin);

}
