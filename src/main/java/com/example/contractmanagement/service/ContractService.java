package com.example.contractmanagement.service;

import java.util.List;

import com.example.contractmanagement.dto.ContractAssignmentDTO;
import com.example.contractmanagement.dto.ContractDraftDTO;
import com.example.contractmanagement.model.Contract;

public interface ContractService {
    Contract draftContract(ContractDraftDTO contractDraftDTO, Long creatorId);
    List<Contract> getAssignableContracts();
    Contract assignContract(ContractAssignmentDTO assignmentDTO);
}