package org.cloud.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cloud.entity.OmsHrEmailEntity;
import org.cloud.service.OmsHrEmailService;
import org.cloud.vo.OmsHrEmailVO;
import org.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/hr/email")
public class HrMailController {

    @Autowired
    private OmsHrEmailService hrEmailService;

    @GetMapping("/list")
    public R list() {
        List<OmsHrEmailEntity> list = hrEmailService.list();
        return R.ok().put("data", list);
    }

    @PostMapping("/add")
    public R add(@RequestBody @Valid OmsHrEmailVO vo) {

        OmsHrEmailEntity dto = hrEmailService.getOne(
                new QueryWrapper<OmsHrEmailEntity>()
                        .eq("email", vo.getEmail())
        );
        // 不重名就创建，重名就报错
        if (dto == null) {
            dto = new OmsHrEmailEntity();
            dto.setEmail(vo.getEmail());
            hrEmailService.save(dto);
            return R.ok();
        }

        return R.error("邮箱已经存在");

    }

    @PostMapping("/del")
    public R del(@RequestParam String ids) {

        if (StrUtil.isEmpty(ids)) {
            return R.error("没有收到任何 id");
        }

        hrEmailService.removeByIds(Arrays.asList(ids.split(",")));

        return R.ok();

    }

}
