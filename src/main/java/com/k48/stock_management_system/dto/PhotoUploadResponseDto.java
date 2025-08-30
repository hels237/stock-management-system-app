package com.k48.stock_management_system.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PhotoUploadResponseDto {
    private String url;
    private String publicId;
    private String message;
}