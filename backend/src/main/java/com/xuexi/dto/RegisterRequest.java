package com.xuexi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String phone;
    @NotBlank
    @Size(min = 6)
    private String password;
    @NotBlank
    private String nickname;
}
