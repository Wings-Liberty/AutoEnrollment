package org.cloud.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dingtalk.api.response.OapiV2DepartmentListparentbyuserResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.Constant;
import org.cloud.entity.OmsEmpInfoEntity;
import org.cloud.service.FmsEnrollRequestService;
import org.cloud.service.OmsEmpInfoService;
import org.cloud.utils.ding.DingUtil;
import org.cloud.utils.res.EventRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DingEventCallbackService {

    @Autowired
    private FmsEnrollRequestService enrollRequestService;

    @Autowired
    private OmsEmpInfoService empInfoService;

    public EventRes process(JSONObject eventJson) throws ApiException {
        // 1. 获取事件类型
        String eventType = eventJson.getString("EventType");

        // 根据不同类型进行处理
        if (eventType.equals(Constant.DING.USER_DEPT_MODIFY_EVENT)) {

            String userIdStr = eventJson.getString("UserId");

            String[] userIds = userIdStr.replace("[", "").replace("]", "").split(",");

            log.info("【收到的用户 id 列表】{}", userIdStr);

            for (String userId : userIds) {

                userId = userId.replace("\"", "");

                // 1. 获取每个 userid 所属的父级部门id
                List<OapiV2DepartmentListparentbyuserResponse.DeptParentResponse> deptsList = DingUtil.listAllParentDeptIds(userId);

                // 2. 检查里面有没有带 “研发” 的部门
                boolean inDevDept = DingUtil.hasNeedKeyInThese("研发", deptsList);

                if (inDevDept && notHoldResource(userId)) { // 用户在研发群，且没有被分配过资源。视为收到入职申请
                    // 3. 创建入职申请（createIfAbsent）
                    OapiV2UserGetResponse response = DingUtil.getUserDetail(userId);

                    log.info("【创建入职请求】入职用户为 {}", response.getBody());

                    enrollRequestService.createIfAbsent(userId, response.getResult());
                }
            }

//            // 1. 如果用户进入研发群，视为收到入职申请
//            String userIds = eventJson.getString("UserId");
//
//            String[] userid = userIds.replace("[", "").replace("]", "").split(",");
//
//            log.info("【收到的用户 id 列表】{}", userIds);
//
//            // 2. 获取开发部 id
//            List<Long> devDeptIds = DingUtil.getDevDeptId();
//
//            log.info("【获取开发部 id】{}", Arrays.toString(devDeptIds.toArray()));
//
//            // 3. 对进入开发部的 userid 进行入职申请
//            for (String id : userid) {
//
//                id = id.replace("\"", "");
//
//                OapiV2UserGetResponse response = DingUtil.getUserDetail(id);
//
//                System.out.println(response.getBody());
//
//                if(response.getResult() == null || response.getResult().getDeptIdList() == null){
//                    log.info("【当前用户不属于任何部门】");
//                    return null;
//                }
//
//                List<Long> res = response.getResult().getDeptIdList();
//
//                log.info("【获取用户所属部门 id】{}", Arrays.toString(res.toArray()));
//
//                res = res.stream()
//                        .filter(devDeptIds::contains)
//                        .collect(Collectors.toList());
//
//                if (CollUtil.isNotEmpty(res) && notHoldResource(id)) { // 用户在研发群，且没有被分配过资源。视为收到入职申请
//                    // 2. 创建入职申请（createIfAbsent）
//                    enrollRequestService.createIfAbsent(id, response.getResult());
//                }
//            }

        } else if (eventType.equals(Constant.DING.USER_LEFT_GROUP) || eventType.equals(Constant.DING.USER_REMOVE_GROUP)) {

        }
        return null;
    }

    private boolean notHoldResource(String userId) {
        OmsEmpInfoEntity one = empInfoService.getOne(
                new QueryWrapper<OmsEmpInfoEntity>()
                        .eq("ding_user_id", userId)
        );

        if (one == null || one.getStatus().equals(Constant.EMP_STATUS.QUIT)) {
            return true; // 如果查不到数据或人员已经离职，就视为没有分配过资源
        }
        return false;
    }
}
