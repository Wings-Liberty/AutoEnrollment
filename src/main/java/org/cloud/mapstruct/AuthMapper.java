package org.cloud.mapstruct;

import org.cloud.entity.OmsAdminInfoEntity;
import org.cloud.vo.AdminLoginVO;
import org.cloud.vo.AdminRegisterVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    OmsAdminInfoEntity toAdminInfo(AdminLoginVO vo);

    OmsAdminInfoEntity toAdminInfo(AdminRegisterVO vo);


}
