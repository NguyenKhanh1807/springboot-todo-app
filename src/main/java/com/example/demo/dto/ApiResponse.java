package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// Annotation này giúp bỏ qua các trường có giá trị null khi chuyển thành JSON
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    // Mã 1000 mặc định cho các request thành công
    @Builder.Default
    private int code = 1000;

    private String message;

    private T result;
}