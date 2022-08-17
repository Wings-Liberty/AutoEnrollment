package org.cloud.service.impl;

import org.cloud.utils.context.AccountContext;
import org.cloud.utils.res.CreateAccountRes;
import org.springframework.stereotype.Component;

/**
 * 自动入职服务
 * <p>
 * 负责完成各种账号的创建
 */
@Component
public class AutoEnrollService {

    public CreateAccountRes createAccount(AccountContext context) {

        // 给管理员创建账号时不需要创建所有账号

        // 关联 钉钉，创建 git，svn，ad 域账号

        return new CreateAccountRes();

    }


}
