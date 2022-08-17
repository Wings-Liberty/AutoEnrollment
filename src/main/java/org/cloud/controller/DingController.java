package org.cloud.controller;

import com.dingtalk.api.response.OapiUserListsimpleResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.taobao.api.ApiException;
import org.cloud.entity.OmsAdminInfoEntity;
import org.cloud.service.OmsAdminInfoService;
import org.cloud.utils.ding.DingUtil;
import org.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ding")
public class DingController {

    @Autowired
    private OmsAdminInfoService adminInfoService;

    @GetMapping("/hr/list")
    public R list() throws ApiException {
        // 1. 递归向下查询所有带人力俩字的部门 id

        // 2. 获取 人力 部门 下的所有人员列表

        // 3. 筛选出不是 本系统管理员的人力人员

        List<OapiUserListsimpleResponse.ListUserSimpleResponse> allEmp = new ArrayList<>();

        LinkedList<OapiV2DepartmentListsubResponse.DeptBaseResponse> curDept = new LinkedList<>(DingUtil.listChildDeptIdsBy(null));

        curDept.forEach(dept -> {
            System.out.println(dept.getDeptId() + " " + dept.getName());
        });
//
        while (curDept.size() > 0) {
            OapiV2DepartmentListsubResponse.DeptBaseResponse dept = curDept.poll();

            Long deptId = dept.getDeptId();

            if(dept.getName().contains("人力")){
                allEmp.addAll(DingUtil.listEmpByDeptId(deptId));
            }

            curDept.addAll(DingUtil.listChildDeptIdsBy(deptId));

        }

//        allEmp.forEach(emp -> {
//            System.out.println(emp.getUserid() + " " + emp.getName());
//        });

        Set<String> set = adminInfoService.list().stream()
                .map(OmsAdminInfoEntity::getDingUserId)
                .collect(Collectors.toSet());

        allEmp = allEmp.stream().filter(emp -> !set.contains(emp.getUserid())).collect(Collectors.toList());

        return R.ok().put("data", allEmp);
    }

}
