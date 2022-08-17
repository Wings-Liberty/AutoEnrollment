package org.cloud.mapstruct;

import javax.annotation.Generated;
import org.cloud.entity.OmsAdminInfoEntity;
import org.cloud.utils.context.AccountContext;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-08-17T20:15:02+0800",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 1.8.0_311 (Oracle Corporation)"
)
public class EnrollMapperImpl implements EnrollMapper {

    @Override
    public AccountContext toAccountContext(OmsAdminInfoEntity adminInfo) {
        if ( adminInfo == null ) {
            return null;
        }

        AccountContext accountContext = new AccountContext();

        return accountContext;
    }
}
