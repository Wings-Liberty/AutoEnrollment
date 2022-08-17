package org.cloud.dao;

import org.cloud.entity.OmsAdminInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户管理系统
管理员信息表。主要记录管理员在本系统的用户名和密码，还有管理员在钉钉的 userid
 * 
 * @author cx
 * @email chang_xu_@outlook.com
 * @date 2022-08-14 11:08:18
 */
@Mapper
public interface OmsAdminInfoDao extends BaseMapper<OmsAdminInfoEntity> {
	
}
