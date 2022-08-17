package org.cloud.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cloud.constant.Constant;
import org.cloud.entity.FmsEnrollRequestEntity;
import org.cloud.service.FmsEnrollRequestService;
import org.cloud.vo.PageVO;
import org.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/enroll")
public class EnrollReqController {

    @Autowired
    private FmsEnrollRequestService enrollRequestService;

    /**
     * 获取待入职员工（新人）列表
     */
    @PostMapping("/list")
    public R listEnrollReqs(@RequestBody PageVO pageVO) {

        Page<FmsEnrollRequestEntity> page = enrollRequestService.page(
                new Page<FmsEnrollRequestEntity>()
                        .setSize(pageVO.getPageSize())
                        .setCurrent(pageVO.getCurrentPage()),
                new QueryWrapper<FmsEnrollRequestEntity>().notIn("status", Constant.ENROLL_STATUS.FINISHED, Constant.ENROLL_STATUS.REFUSE)
        );

        return R.ok().put("page", page);

    }

    /**
     * 批量拒绝入职请求
     */
    @PostMapping("/refuse")
    public R delEnrollReq(@RequestParam("userIds") String userIds){

        List<String> ids = Arrays.asList(userIds.split(","));

        if (CollUtil.isNotEmpty(ids)){ // 如果没有 userids 就不走拒绝入职请求流程
            enrollRequestService.refuseReqs(ids);
        }

        return R.ok();

    }

    @PostMapping("/entry")
    public R doEntry(@RequestParam("userIds") String userIds){

        List<String> ids = Arrays.asList(userIds.split(","));

        if (CollUtil.isNotEmpty(ids)){ // 如果没有 userids 就不走拒绝入职流程
            enrollRequestService.doEntry(ids);
        }

        return R.ok();

    }

}
