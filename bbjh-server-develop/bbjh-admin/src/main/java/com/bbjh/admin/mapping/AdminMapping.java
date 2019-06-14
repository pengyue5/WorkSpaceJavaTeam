package com.bbjh.admin.mapping;

import com.bbjh.admin.api.admin.sys.AdminAddRequest;
import com.bbjh.admin.api.admin.sys.AdminModifyRequest;
import com.bbjh.common.model.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapping {

    Admin convert(AdminAddRequest request);

    Admin convert(AdminModifyRequest request);
}
