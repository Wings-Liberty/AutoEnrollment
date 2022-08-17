package org.cloud.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class OmsHrEmailVO {

    @NotEmpty
    @Email
    private String email;

}
