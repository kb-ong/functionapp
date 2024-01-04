package com.example.test;

public class EInvoice {
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    private String supplierId;

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    private String supplierName;

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getPolicyAmount() {
        return policyAmount;
    }

    public void setPolicyAmount(String policyAmount) {
        this.policyAmount = policyAmount;
    }

    public String getPollcyCurrency() {
        return pollcyCurrency;
    }

    public void setPollcyCurrency(String pollcyCurrency) {
        this.pollcyCurrency = pollcyCurrency;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    private String policyNo;
    private String policyAmount;
    private String pollcyCurrency;
    private String dateCreated;

}
