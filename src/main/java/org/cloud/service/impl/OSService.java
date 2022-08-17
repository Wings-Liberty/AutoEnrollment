package org.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.Constant;
import org.cloud.entity.OmsAdminInfoEntity;
import org.cloud.service.OmsAdminInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OSService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OmsAdminInfoService adminInfoService;

    public void initRootUser() {

//        try {
//            TimeUnit.SECONDS.sleep(5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // 1. 如果有 root 就不处理
        OmsAdminInfoEntity root = adminInfoService.getOne(
                new QueryWrapper<OmsAdminInfoEntity>()
                        .eq("username", "root")
        );

        // 2. 如果没有就创建
        if(root == null){
            root = new OmsAdminInfoEntity();
            root.setRole(Constant.AUTH_ROLE.ROOT);
            root.setUsername("root");
            root.setPassword(passwordEncoder.encode("root"));

            adminInfoService.save(root);

            log.info("【缺失 root 用户】创建 root 用户。username = root, password = root");
        }
    }


}
