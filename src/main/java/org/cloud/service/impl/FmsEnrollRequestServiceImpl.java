package org.cloud.service.impl;

import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.Constant;
import org.cloud.entity.OmsEmpInfoEntity;
import org.cloud.service.OmsEmpInfoService;
import org.cloud.service.OmsHrEmailService;
import org.cloud.utils.ThreadPoolUtil;
import org.cloud.utils.ding.DingUtil;
import org.cloud.utils.res.ResourceAllocateRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.cloud.dao.FmsEnrollRequestDao;
import org.cloud.entity.FmsEnrollRequestEntity;
import org.cloud.service.FmsEnrollRequestService;


@Service("fmsEnrollRequestService")
@Slf4j
public class FmsEnrollRequestServiceImpl extends ServiceImpl<FmsEnrollRequestDao, FmsEnrollRequestEntity> implements FmsEnrollRequestService {

    @Autowired
    private OmsEmpInfoService empInfoService;

    @Autowired
    private OmsHrEmailService hrEmailService;

    private static final Object lock = new Object();

    @Override
    public void createIfAbsent(String userId, OapiV2UserGetResponse.UserGetResponse user) {

        OmsEmpInfoEntity empInfo = empInfoService.getOne(
                new QueryWrapper<OmsEmpInfoEntity>()
                        .eq("ding_user_id", userId)
        );

        // 用户没有资源分配记录或之前离过职的，可以发送入职申请
        if (empInfo == null || empInfo.getStatus().equals(Constant.EMP_STATUS.QUIT)) {

            synchronized (lock) {
                // 发送申请前，把这个用户之前发送的入职申请全删了
                this.remove(
                        new QueryWrapper<FmsEnrollRequestEntity>()
                                .eq("ding_user_id", userId)
                );

                FmsEnrollRequestEntity entity = new FmsEnrollRequestEntity();

                entity.setDingUserId(userId);
                entity.setStatus(Constant.ENROLL_STATUS.UNTREATED);
                entity.setUsername(user.getName());
                entity.setPhone(user.getMobile());
                entity.setEmail(user.getEmail());

                this.save(entity);
            }

        } else {
            log.info("user id {} 已经分配过资源或处于非离职状态", userId);
        }
    }

    @Override
    public void refuseReqs(List<String> ids) {
        // 把目标的请求状态改为拒绝
        updateStatusByDingUserId(ids, Constant.ENROLL_STATUS.REFUSE);
    }

    @Override
    public void doEntry(List<String> userids) {

        // 1. 把这些人的入职请求状态改为正在分配资源
        updateStatusByDingUserId(userids, Constant.ENROLL_STATUS.ALLOCATING);

        // 2. 为这些人创建资源（任务丢线程池里）
        userids.forEach(userid -> {
            ThreadPoolUtil.get().submit(() -> {

                // 创建账号和分配资源
                ResourceAllocateRes resourceAllocateRes = createAccountAndCreateResource(userid);

                // 修改入职请求状态为 “已完成”
                updateStatusByDingUserId(Collections.singletonList(userid), Constant.ENROLL_STATUS.FINISHED);

                // 创建用户账号或修改人员状态
                try {
                    OapiV2UserGetResponse userDetail = DingUtil.getUserDetail(userid);
                    createOrUpdateEmpInfo(userDetail.getResult(), resourceAllocateRes);
                } catch (ApiException e) {
                    e.printStackTrace();
                }

                // 向入职人员发送钉钉消息
                try {
                    DingUtil.sendDingTextMsg(userid, "已经完入职和资源分配流程");
                } catch (ApiException e) {
                    e.printStackTrace();
                }

                // 向人力主管发送邮件
                hrEmailService.list().forEach(hr -> MailUtil.send(hr.getEmail(), "入职通知", "已经完入职和资源分配流程", false));

            });
        });

        // TODO: 2022/8/15 final 设置一个事件状态查询的定时任务，定时查询这些人的资源分配状态。如果长时间没有响应，就把状态改为 error
    }

    private void createOrUpdateEmpInfo(OapiV2UserGetResponse.UserGetResponse user, ResourceAllocateRes resourceAllocateRes) {

        // TODO: 2022/8/17 存在并发问题，因为 createIfNotAbsent 不是原子的


        OmsEmpInfoEntity one = empInfoService.getOne(
                new QueryWrapper<OmsEmpInfoEntity>()
                        .eq("ding_user_id", user.getUserid())
        );

        if (one == null) {
            one = new OmsEmpInfoEntity();
            one.setUsername(user.getName());
            one.setStatus(Constant.EMP_STATUS.WORKING);
            one.setDingUserId(user.getUserid());
            one.setUnifiedAuthPassword(resourceAllocateRes.getUnified_auth_password());
            one.setUnifiedAuthUsername(resourceAllocateRes.getUnified_auth_username());
            one.setPhone(user.getMobile());

            empInfoService.save(one);
        }else {
            one.setStatus(Constant.EMP_STATUS.WORKING);
            one.setUnifiedAuthPassword(resourceAllocateRes.getUnified_auth_password());
            one.setUnifiedAuthUsername(resourceAllocateRes.getUnified_auth_username());

            empInfoService.updateById(one);
        }

    }

    private void updateStatusByDingUserId(List<String> userids, int status) {
        // 把目标的请求状态改为拒绝
        FmsEnrollRequestEntity update = new FmsEnrollRequestEntity();
        update.setStatus(status);

        this.update(
                update,
                new UpdateWrapper<FmsEnrollRequestEntity>()
                        .in("ding_user_id", userids)
        );

        log.info("【入职请求状态修改完成】状态改为 {}", status);
    }

    /**
     * 创建账号和分配资源的核心方法
     * <p>
     * 创建 AD 域，创建虚拟机
     *
     * @param userid 钉钉的 userid
     */
    private ResourceAllocateRes createAccountAndCreateResource(String userid) {
        // TODO: 2022/8/15 填代码
        log.info("【创建账号分配资源】假设已经完成了这些工作");
        ResourceAllocateRes res = new ResourceAllocateRes();
        res.setUnified_auth_username("changxu");
        res.setUnified_auth_password("6cloud6.com");

        return res;
    }
}