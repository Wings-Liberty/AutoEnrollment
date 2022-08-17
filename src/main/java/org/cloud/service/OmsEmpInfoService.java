package org.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.entity.OmsEmpInfoEntity;
import org.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 用户管理系统
职员表。保存至少分配过一次资源的人员信息，主要包含钉钉关联信息，统一认证账号信息（git，svn，ad域等账号的用户名密码都统一用这个）。创建普通管理员时也应该把他的信息加到这张表里
 *
 * @author cx
 * @email chang_xu_@outlook.com
 * @date 2022-08-14 11:08:18
 */
public interface OmsEmpInfoService extends IService<OmsEmpInfoEntity> {

    void quitAndReleaseResource(List<String> userIds);
}

