package org.cloud;

import cn.hutool.extra.mail.MailUtil;

public class HutoolMailTest {

    public static void main(String[] args) {
        MailUtil.send("1434948003@qq.com", "测试", "邮件来自Hutool测试", false);
    }

}
