package org.cloud.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户管理系统
职员表。保存至少分配过一次资源的人员信息，主要包含钉钉关联信息，统一认证账号信息（git，svn，ad域等账号的用户名密码都统一用这个）。创建普通管理员时也应该把他的信息加到这张表里
 * 
 * @author cx
 * @email chang_xu_@outlook.com
 * @date 2022-08-14 11:08:18
 */
@Data
@TableName("oms_emp_info")
public class OmsEmpInfoEntity implements Serializable {
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
	 * 部门名，钉钉一致
	 */
	private String deptName;
	/**
	 * 部门 id，和钉钉一致
	 */
	private Integer deptId;
	/**
	 * 在职状态。0 - 正在分配资源，1 - 正在释放资源；2 - 在职；3 - 离职
	 */
	private Integer status;
	/**
	 * 钉钉上的 userid
	 */
	private String dingUserId;
	/**
	 * 统一认证密码
	 */
	private String unifiedAuthPassword;
	/**
	 * 统一认证用户名
	 */
	private String unifiedAuthUsername;

	private String phone;

}
