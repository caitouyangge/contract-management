package com.example.contractmanagement.service;

import java.util.List;

import com.example.contractmanagement.dto.ContractAssignmentDTO;
import com.example.contractmanagement.dto.ContractDraftDTO;
import com.example.contractmanagement.dto.CountersignRequest;
import com.example.contractmanagement.model.Contract;

public interface ContractService {
    Contract draftContract(ContractDraftDTO contractDraftDTO, Long creatorId);
    List<Contract> getAssignableContracts();
    Contract assignContract(ContractAssignmentDTO assignmentDTO);
    List<Contract> getCountersignContracts(Long userId);
    void submitCountersign(CountersignRequest request);
    List<Contract> getContractsReadyForFinalization(Long creatorId);
    Contract finalizeContract(Long contractId, String updatedContent);
    // 在ContractService接口中添加
List<Contract> getPendingApprovalContracts(Long approvalUserId);
Contract approveContract(Long contractId, String approvalResult, String approvalComment);
}
