package org.cloud.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 表格管理系统
表格结果数据行表 / 入职申请表。保存收到的文件提交信息，此表不保存用户填的数据，因为钉钉已经保存了。只保存入职申请的元数据和入职申请的处理进度
 * 
 * @author cx
 * @email chang_xu_@outlook.com
 * @date 2022-08-15 14:35:51
 */
@Data
@TableName("fms_enroll_request")
public class FmsEnrollRequestEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Integer id;
	/**
	 * 钉钉用户的 userid。如果提交人还没有在钉钉群里，那么此字段值未空
	 * // TODO: 2022/8/15 应该给这个字段设置为唯一索引，因为需要保证它只能出现一次
	 */
	private String dingUserId;
	/**
	 * 0 - 未进行入职；1 - 正在分配资源；2 - 分配资源出现异常，异常详情见 error_info 属性；3 - 已处理（虽然为其分配过资源，但不代表这个人还在职，因为它可能已经离职了）；4 - 拒绝入职申请（可以考虑不再在入职申请列表展示已被拒绝的申请）
	 */
	private Integer status;
	/**
	 * 如果分配资源过程中出现异常，这个属性应该记录原因
	 */
	private String errorInfo;
	/**
	 * 如果入职过一次，这个字段应该记录入职人员的 emp_info 的主键，便于在入职申请表中直接查询已经入职人员的信息
	 */
	private Integer empInfoId;

	private String username;

	private String phone;

	private String email;

}
