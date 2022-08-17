package org.cloud.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.cloud.entity.OmsAdminInfoEntity;
import org.cloud.mapstruct.AuthMapper;
import org.cloud.mapstruct.EnrollMapper;
import org.cloud.constant.Constant;
import org.cloud.properties.JWTProperties;
import org.cloud.service.OmsAdminInfoService;
import org.cloud.utils.res.CreateAccountRes;
import org.cloud.vo.AdminLoginVO;
import org.cloud.vo.AdminRegisterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private OmsAdminInfoService adminInfoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AutoEnrollService autoEnrollService;

    @Autowired
    private JWTProperties jwtProperties;

    public OmsAdminInfoEntity register(AdminRegisterVO vo) {

        log.info("有用户注册，收到的用户名：{}，收到的密码是：{}", vo.getUsername(), vo.getPassword());

        // 1. 检查是否有重名
        boolean res = !checkMultiUsername(vo.getUsername());

        if (res) {
            String encodePassword = passwordEncoder.encode(vo.getPassword());

            OmsAdminInfoEntity entity = AuthMapper.INSTANCE.toAdminInfo(vo);

            entity.setPassword(encodePassword);
            entity.setRole(Constant.AUTH_ROLE.ADMIN);
            res = adminInfoService.save(entity);

            // 创建账号不应该异步化
            CreateAccountRes createAccountRes = autoEnrollService.createAccount(EnrollMapper.INSTANCE.toAccountContext(entity));
            // TODO: 2022/8/14 保存账号

            return createAccountRes.isSuccess() && res ? entity : null;
        }

        return null;
    }

    public Map<String, String> login(AdminLoginVO vo) throws AuthenticationException {

        // 1. 校验用户名和密码
        log.info("有用户登录，收到的用户名：{}，收到的密码是：{}", vo.getUsername(), vo.getPassword());

        OmsAdminInfoEntity adminInfo = adminInfoService.getOne(
                new QueryWrapper<OmsAdminInfoEntity>()
                        .eq("username", vo.getUsername())
        );

        if (adminInfo == null || !passwordEncoder.matches(vo.getPassword(), adminInfo.getPassword())) {
            throw new AuthenticationCredentialsNotFoundException("用户名或密码错误");
        }

        // 2. 创建 token
        String token = createToken(adminInfo);

        Integer uid = adminInfo.getId();

        Map<String, String> res = new HashMap<>();
        res.put("token", token);
//        res.put("uid", uid.toString());
//        res.put("username", adminInfo.getUsername());

        return res;
    }

    /**
     * 如果没重名就返回 true
     */
    private boolean checkMultiUsername(String username) {
        OmsAdminInfoEntity admin = adminInfoService.getOne(
                new QueryWrapper<OmsAdminInfoEntity>()
                        .eq("username", username)
        );
        return admin != null;
    }

    private String createToken(OmsAdminInfoEntity adminInfo) {
        Map<String, Object> map = new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;

            {
                put("uid", adminInfo.getId());
                put("expire_time", System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15); // 1s * 60 * 60 * 24 * 15 即 15 天
                put("username", adminInfo.getUsername());
                put("authentication", adminInfo.getRole());
            }
        };

        String accessToken = JWT.create()
                .setNotBefore(DateUtil.date())
                .setExpiresAt(DateUtil.date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15)) // 15 天过期
                .addPayloads(map)
                .setKey(jwtProperties.getKeyBytes())
                .sign();

        log.info("【创建 access_token】{}", accessToken);

        return accessToken;
    }

}
