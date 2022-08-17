package org.cloud;

import org.cloud.service.impl.OSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements ApplicationRunner {

    @Autowired
    private OSService osService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class);

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 创建 root 账号
        osService.initRootUser();
    }
}
