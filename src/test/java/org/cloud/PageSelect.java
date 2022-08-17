package org.cloud;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cloud.dao.FmsEnrollRequestDao;
import org.cloud.entity.FmsEnrollRequestEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PageSelect {

    @Autowired
    private FmsEnrollRequestDao dao;

    @Test
    public void test() {

        Page<FmsEnrollRequestEntity> page = dao.selectPage(new Page<>(1, 4)
                , new QueryWrapper<>()
        );

        System.out.println(page);

        System.out.println(JSONUtil.parse(page).toString());
    }

}
