package com.example.contractmanagement.dto;

public class ContractFinalizeRequest {
    private Long contractId;
    private String updatedContent;
    // getters and setters
    public Long getContractId() {
        return contractId;
    }
    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }
    public String getUpdatedContent() {
        return updatedContent;
    }
    public void setUpdatedContent(String updatedContent) {
        this.updatedContent = updatedContent;
    }
}
