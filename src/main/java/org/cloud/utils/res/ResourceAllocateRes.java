package org.cloud.utils.res;

import lombok.Data;

@Data
public class ResourceAllocateRes implements Res{

    private String unified_auth_username;
    private String unified_auth_password;

    @Override
    public boolean isSuccess() {
        return true;
    }
}
