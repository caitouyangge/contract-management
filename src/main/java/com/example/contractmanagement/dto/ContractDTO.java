package com.example.contractmanagement.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ContractDTO {
    @NotBlank(message = "合同名称不能为空")
    private String name;
    
    @NotBlank(message = "客户名称不能为空")
    private String customer;
    
    @NotNull(message = "开始时间不能为空")
    private Date startDate;
    
    @NotBlank(message = "合同内容不能为空")
    private String content;
    
    private MultipartFile attachment;

    private Long creatorId; // 新增creatorId字段
}