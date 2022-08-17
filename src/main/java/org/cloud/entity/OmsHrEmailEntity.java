package org.cloud.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 人力主管的邮箱列表
 *
 * @author cx
 * @email chang_xu_@outlook.com
 * @date 2022-08-14 11:08:18
 */
@Data
@TableName("oms_hr_email")
public class OmsHrEmailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Integer id;

	private String email;

}
