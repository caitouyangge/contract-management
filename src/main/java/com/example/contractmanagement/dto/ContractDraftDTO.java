package com.example.contractmanagement.dto;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ContractDraftDTO {
    @NotBlank(message = "合同名称不能为空")
    private String contractName;
    
    @NotBlank(message = "客户名称不能为空")
    private String customerName;
    
    @NotNull(message = "开始时间不能为空")
    private LocalDate startDate;
    
    @NotBlank(message = "合同内容不能为空")
    private String content;
    
    private MultipartFile file;

    // 新增creatorId字段（不需要验证注解）
    private Long creatorId;

}