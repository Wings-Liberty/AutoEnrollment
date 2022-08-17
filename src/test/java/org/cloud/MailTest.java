package org.cloud;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MailTest {

    @Autowired
    private JavaMailSender sender;

    @Test
    public void sendMessage() {

        String from = "cx1434948003@163.com";
        String to = "1434948003@qq.com";

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject("主题");
        simpleMailMessage.setText("正文");
        simpleMailMessage.setFrom(from);
        sender.send(simpleMailMessage);
    }


}
