package org.cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import org.cloud.entity.FmsEnrollRequestEntity;

import java.util.List;

/**
 * 表格管理系统
表格结果数据行表 / 入职申请表。保存收到的文件提交信息，此表不保存用户填的数据，因为钉钉已经保存了。只保存入职申请的元数据和入职申请的处理进度
 *
 * @author cx
 * @email chang_xu_@outlook.com
 * @date 2022-08-15 14:35:51
 */
public interface FmsEnrollRequestService extends IService<FmsEnrollRequestEntity> {


    void createIfAbsent(String userId, OapiV2UserGetResponse.UserGetResponse result);

    void refuseReqs(List<String> ids);

    void doEntry(List<String> userids);

}

