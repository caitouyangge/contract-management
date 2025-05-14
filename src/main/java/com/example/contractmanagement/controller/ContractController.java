package com.example.contractmanagement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.contractmanagement.dto.ContractAssignmentDTO;
import com.example.contractmanagement.dto.ContractDraftDTO;
import com.example.contractmanagement.model.Contract;
import com.example.contractmanagement.service.ContractService;
import com.example.contractmanagement.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {
    
    private final ContractService contractService;
    private final UserService userService;
    
    public ContractController(ContractService contractService, UserService userService) {
        this.contractService = contractService;
        this.userService = userService;
    }
    
    // ContractController.java
    @PostMapping("/draft")
    public String draftContract(@ModelAttribute ContractDraftDTO dto) {
        // 直接使用前端传来的creatorId
        contractService.draftContract(dto, dto.getCreatorId());
        return "success";
    }
    @GetMapping("/assignable")
    public ResponseEntity<List<Contract>> getAssignableContracts() {
        List<Contract> contracts = contractService.getAssignableContracts();
        return ResponseEntity.ok(contracts);
    }

    @PostMapping("/assign")
    public ResponseEntity<Contract> assignContract(
            @Valid @RequestBody ContractAssignmentDTO assignmentDTO) {
        Contract assignedContract = contractService.assignContract(assignmentDTO);
        return ResponseEntity.ok(assignedContract);
    }
}