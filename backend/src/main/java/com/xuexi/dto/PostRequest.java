package com.xuexi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostRequest {
    @NotBlank
    private String type;
    @NotBlank
    private String content;
    private String imageUrl;
}
