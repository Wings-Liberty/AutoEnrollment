package org.cloud.mapstruct;

import org.cloud.entity.OmsAdminInfoEntity;
import org.cloud.utils.context.AccountContext;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 把各种类转换成创建入职账号时需要的上下文对象
 *
 * 创建账号方法见 {@link org.cloud.service.impl.AutoEnrollService}
 */
@Mapper
public interface EnrollMapper {

    EnrollMapper INSTANCE = Mappers.getMapper(EnrollMapper.class);

    AccountContext toAccountContext(OmsAdminInfoEntity adminInfo);

}
