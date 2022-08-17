package org.cloud.controller;

import org.cloud.entity.OmsAdminInfoEntity;
import org.cloud.service.impl.AuthService;
import org.cloud.vo.AdminLoginVO;
import org.cloud.vo.AdminRegisterVO;
import org.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public R login(@RequestBody @Valid AdminLoginVO loginUser) throws AuthenticationException {

        Map<String, String> map = authService.login(loginUser);

        return R.ok().put("data", map);
    }

    /**
     * 注册
     * <p>
     * // TODO: 2022/8/15 目前仅做了判空校验
     */
    @PostMapping("/register")
    public R register(@RequestBody @Valid AdminRegisterVO vo) {

        OmsAdminInfoEntity register = authService.register(vo);

        return register != null ? R.ok() : R.error("用户名重复了或创建账号时出现了异常");

    }


}
