package org.cloud.utils.context;

import lombok.Data;

@Data
public class UserInfoContext {

    private String username;

    // 钉钉的 userid
    private String dingUserId;

    // 数据库主键
    private Integer userId;

}
