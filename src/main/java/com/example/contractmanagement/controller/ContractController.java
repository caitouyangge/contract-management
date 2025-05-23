package com.example.contractmanagement.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.contractmanagement.dto.ContractAssignmentDTO;
import com.example.contractmanagement.dto.ContractDraftDTO;
import com.example.contractmanagement.dto.ContractFinalizeRequest;
import com.example.contractmanagement.dto.CountersignRequest;
import com.example.contractmanagement.model.Contract;
import com.example.contractmanagement.model.User;
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
    @PostMapping("/assign")
    public ResponseEntity<String> assignContract(
            @Valid @RequestBody ContractAssignmentDTO dto) {
        
        // 验证会签人员
        if (dto.getCountersignUserIds() == null || dto.getCountersignUserIds().isEmpty()) {
            return ResponseEntity.badRequest().body("至少需要一名会签人员");
        }
        
        // 验证审批人员
        if (dto.getApprovalUserId() == null) {
            return ResponseEntity.badRequest().body("需要指定审批人员");
        }
        
        // 验证用户角色
        List<User> assignableUsers = userService.findAssignableUsers();
        Set<Long> validUserIds = assignableUsers.stream()
            .map(User::getId)
            .collect(Collectors.toSet());
            
        if (!validUserIds.containsAll(dto.getCountersignUserIds()) || 
            !validUserIds.contains(dto.getApprovalUserId())) {
            return ResponseEntity.badRequest().body("只能选择管理员或操作员");
        }
        
        Contract contract = contractService.assignContract(dto);
        return ResponseEntity.ok("分配成功");
    }
    
    @GetMapping("/assignable")
    public ResponseEntity<List<Contract>> getAssignableContracts() {
        List<Contract> contracts = contractService.getAssignableContracts();
        return ResponseEntity.ok(contracts);
    }
    @GetMapping("/countersign")
    public ResponseEntity<List<Contract>> getCountersignContracts(@RequestParam Long userId) {
        List<Contract> contracts = contractService.getCountersignContracts(userId);
        return ResponseEntity.ok(contracts);
    }

    @PostMapping("/countersign")
    public ResponseEntity<String> submitCountersign(
            @RequestBody CountersignRequest request) {
        contractService.submitCountersign(request);
        return ResponseEntity.ok("会签意见提交成功");
    }
    @GetMapping("/finalize")
    public ResponseEntity<List<Contract>> getContractsReadyForFinalization(
            @RequestParam Long creatorId) {
        List<Contract> contracts = contractService.getContractsReadyForFinalization(creatorId);
        return ResponseEntity.ok(contracts);
    }

    @PostMapping("/finalize")
    public ResponseEntity<String> finalizeContract(
            @RequestBody ContractFinalizeRequest request) {
        contractService.finalizeContract(request.getContractId(), request.getUpdatedContent());
        return ResponseEntity.ok("合同定稿成功");
    }
    // 在ContractController.java中添加
@GetMapping("/pending-approval")
public ResponseEntity<List<Contract>> getPendingApprovalContracts(@RequestParam Long approvalUserId) {
    List<Contract> contracts = contractService.getPendingApprovalContracts(approvalUserId);
    return ResponseEntity.ok(contracts);
}

@PostMapping("/{contractId}/approve")
public ResponseEntity<String> approveContract(
        @PathVariable Long contractId,
        @RequestBody Map<String, String> request) {
    
    String result = request.get("approvalResult");
    String comment = request.get("approvalComment");
    
    if (!"approve".equals(result) && !"reject".equals(result)) {
        return ResponseEntity.badRequest().body("审批结果无效");
    }
    
    contractService.approveContract(contractId, result, comment);
    return ResponseEntity.ok("审批提交成功");
}
}