package org.cloud.mapstruct;

import javax.annotation.Generated;
import org.cloud.entity.OmsAdminInfoEntity;
import org.cloud.vo.AdminLoginVO;
import org.cloud.vo.AdminRegisterVO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-08-17T20:15:02+0800",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 1.8.0_311 (Oracle Corporation)"
)
public class AuthMapperImpl implements AuthMapper {

    @Override
    public OmsAdminInfoEntity toAdminInfo(AdminLoginVO vo) {
        if ( vo == null ) {
            return null;
        }

        OmsAdminInfoEntity omsAdminInfoEntity = new OmsAdminInfoEntity();

        omsAdminInfoEntity.setUsername( vo.getUsername() );
        omsAdminInfoEntity.setPassword( vo.getPassword() );

        return omsAdminInfoEntity;
    }

    @Override
    public OmsAdminInfoEntity toAdminInfo(AdminRegisterVO vo) {
        if ( vo == null ) {
            return null;
        }

        OmsAdminInfoEntity omsAdminInfoEntity = new OmsAdminInfoEntity();

        omsAdminInfoEntity.setUsername( vo.getUsername() );
        omsAdminInfoEntity.setPassword( vo.getPassword() );
        omsAdminInfoEntity.setDingUserId( vo.getDingUserId() );

        return omsAdminInfoEntity;
    }
}
