package org.cloud.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户管理系统
管理员信息表。主要记录管理员在本系统的用户名和密码，还有管理员在钉钉的 userid
 * 
 * @author cx
 * @email chang_xu_@outlook.com
 * @date 2022-08-14 11:08:18
 */
@Data
@TableName("oms_admin_info")
public class OmsAdminInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Integer id;
	/**
	 * 用户名，和钉钉一致
	 */
	private String username;
	/**
	 * 密码，经过加密的
	 */
	private String password;
	/**
	 * 角色。用于系统的权限访问控制，不做复杂的权限访问控制，仅根据角色判断是否有权限访问接口。当此用户有多个角色时用 ';' 分割
	 */
	private String role;
	/**
	 * 钉钉上的 userid
	 */
	private String dingUserId;

}
