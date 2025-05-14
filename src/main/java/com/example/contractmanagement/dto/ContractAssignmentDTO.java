package com.example.contractmanagement.dto;

import java.util.List;

import lombok.Data;

@Data
public class ContractAssignmentDTO {
    private Long contractId;
    private List<Long> countersignUserIds;
    private Long approvalUserId;
}