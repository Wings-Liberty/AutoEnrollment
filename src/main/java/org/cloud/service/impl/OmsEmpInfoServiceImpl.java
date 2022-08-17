package org.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.Constant;
import org.cloud.dao.OmsEmpInfoDao;
import org.cloud.entity.OmsEmpInfoEntity;
import org.cloud.service.OmsEmpInfoService;
import org.cloud.utils.ThreadPoolUtil;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service("omsEmpInfoService")
@Slf4j
public class OmsEmpInfoServiceImpl extends ServiceImpl<OmsEmpInfoDao, OmsEmpInfoEntity> implements OmsEmpInfoService {

    @Override
    public void quitAndReleaseResource(List<String> userIds) {

        // 1. 将其改为释放资源状态
        updateStatusByDingUserId(userIds, Constant.EMP_STATUS.RELEASING);

        // 2. 释放资源，删除账号（扔到线程池里）释放完毕后，把状态改为离职
        userIds.forEach(userid -> {
            ThreadPoolUtil.get().submit(() -> {
                deleteAccountAndReleaseResource(userid);
                updateStatusByDingUserId(Collections.singletonList(userid), Constant.EMP_STATUS.QUIT);
            });
        });

        // TODO: 2022/8/15 final 设置定时任务，查询资源释放结果
    }

    private void deleteAccountAndReleaseResource(String userid) {
        // TODO: 2022/8/15 填写资源释放代码
        log.info("【假设执行过了资源的释放】");
    }

    private void updateStatusByDingUserId(List<String> userids, int status) {

        // 把目标的状态设置为离职
        OmsEmpInfoEntity update = new OmsEmpInfoEntity();
        update.setStatus(status);

        this.update(
                update,
                new UpdateWrapper<OmsEmpInfoEntity>()
                        .in("ding_user_id", userids)
        );
    }
}