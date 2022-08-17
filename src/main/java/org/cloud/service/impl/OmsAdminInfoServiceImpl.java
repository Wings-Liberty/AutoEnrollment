package org.cloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cloud.dao.OmsAdminInfoDao;
import org.cloud.entity.OmsAdminInfoEntity;
import org.cloud.service.OmsAdminInfoService;
import org.cloud.vo.AdminLoginVO;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Map;


@Service("omsAdminInfoService")
public class OmsAdminInfoServiceImpl extends ServiceImpl<OmsAdminInfoDao, OmsAdminInfoEntity> implements OmsAdminInfoService {



}