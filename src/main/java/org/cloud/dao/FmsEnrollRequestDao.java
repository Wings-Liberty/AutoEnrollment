package org.cloud.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.cloud.entity.FmsEnrollRequestEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 表格管理系统
表格结果数据行表 / 入职申请表。保存收到的文件提交信息，此表不保存用户填的数据，因为钉钉已经保存了。只保存入职申请的元数据和入职申请的处理进度
 * 
 * @author cx
 * @email chang_xu_@outlook.com
 * @date 2022-08-15 14:35:51
 */
@Mapper
public interface FmsEnrollRequestDao extends BaseMapper<FmsEnrollRequestEntity> {

    IPage<FmsEnrollRequestEntity> page(IPage<?> page);

}
