package org.cloud.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cloud.constant.Constant;
import org.cloud.entity.FmsEnrollRequestEntity;
import org.cloud.entity.OmsEmpInfoEntity;
import org.cloud.service.OmsEmpInfoService;
import org.cloud.vo.PageVO;
import org.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/emp")
public class EmpInfoController {

    @Autowired
    private OmsEmpInfoService empInfoService;

    /**
     * 分页查询在职人员信息
     */
    @PostMapping("/list")
    public R list(@RequestBody PageVO pageVO) {

        Page<OmsEmpInfoEntity> page = empInfoService.page(
                new Page<OmsEmpInfoEntity>()
                        .setSize(pageVO.getPageSize())
                        .setCurrent(pageVO.getCurrentPage()),
                new QueryWrapper<OmsEmpInfoEntity>().notIn("status", Constant.EMP_STATUS.QUIT)
        );

        return R.ok().put("page", page);

    }

    @PostMapping("/quit")
    public R quit(@RequestParam("userIds") String userIds){
        List<String> ids = Arrays.asList(userIds.split(","));

        if (CollUtil.isNotEmpty(ids)){ // 如果没有 userids 就不走拒绝入职请求流程
            empInfoService.quitAndReleaseResource(ids);
        }

        return R.ok();
    }

}
