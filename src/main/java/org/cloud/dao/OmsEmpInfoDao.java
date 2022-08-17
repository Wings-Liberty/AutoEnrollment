package org.cloud.dao;

import org.cloud.entity.OmsEmpInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户管理系统
职员表。保存至少分配过一次资源的人员信息，主要包含钉钉关联信息，统一认证账号信息（git，svn，ad域等账号的用户名密码都统一用这个）。创建普通管理员时也应该把他的信息加到这张表里
 * 
 * @author cx
 * @email chang_xu_@outlook.com
 * @date 2022-08-14 11:08:18
 */
@Mapper
public interface OmsEmpInfoDao extends BaseMapper<OmsEmpInfoEntity> {
	
}
