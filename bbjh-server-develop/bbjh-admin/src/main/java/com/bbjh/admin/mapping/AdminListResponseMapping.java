package com.bbjh.admin.mapping;

import com.bbjh.admin.api.admin.sys.AdminListResponse;
import com.bbjh.common.model.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminListResponseMapping {

    AdminListResponse convert(Admin admin);
}
