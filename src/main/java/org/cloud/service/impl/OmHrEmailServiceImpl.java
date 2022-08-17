package org.cloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.cloud.dao.OmsHrEmailDao;
import org.cloud.entity.OmsHrEmailEntity;
import org.cloud.service.OmsHrEmailService;
import org.springframework.stereotype.Service;


@Service("omHrEmailService")
@Slf4j
public class OmHrEmailServiceImpl extends ServiceImpl<OmsHrEmailDao, OmsHrEmailEntity> implements OmsHrEmailService {

}