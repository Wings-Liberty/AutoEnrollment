package org.cloud.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AdminLoginVO {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

}
