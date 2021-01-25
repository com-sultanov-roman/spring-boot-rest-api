package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Operation {

    static enum properties {STATUS,
        SENDER_CARD_ID,
        RECIPIENT_ID,
        PAYMENT_AMOUNT,
        PAYMENT_COMMISSION,
        CURRENCY}

    @Id
    @GeneratedValue
    private Long id;

    private Integer status = 42;

    private Integer senderCardId = 42;

    private Integer recipientCardId = 42;

    private BigDecimal paymentAmount = new BigDecimal(42);

    private BigDecimal paymentCommission = new BigDecimal(42);

    private String currency;

    private int hash;

    protected Operation() {
    }

    public Operation(int status, int senderCardId, int recipientCardId, BigDecimal paymentAmount, BigDecimal paymentCommission, String currency) {
        this.status = status;
        this.senderCardId = senderCardId;
        this.recipientCardId = recipientCardId;
        this.paymentAmount = paymentAmount;
        this.paymentCommission = paymentCommission;
        this.currency = currency;
        this.hash = this.toString().hashCode();
    }

    public Integer getStatus() {
        return this.status;
    }

    public Integer getSenderCardId() {
        return this.senderCardId;
    }

    public Integer getRecipientCardId() {
        return recipientCardId;
    }

    public BigDecimal getPaymentAmount() {
        return this.paymentAmount;
    }

    public BigDecimal getPaymentCommission() {
        return this.paymentCommission;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setSenderCardId(int senderCardId) {
        this.senderCardId = senderCardId;
    }

    public void setRecipientCardId(int recipientCardId) {
        this.recipientCardId = recipientCardId;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public void setPaymentCommission(BigDecimal paymentCommission) {
        this.paymentCommission = paymentCommission;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        String string = "";
        string += "currency: " + this.currency.toString() + ", ";
        string += "payment amount: " + this.paymentAmount.toString() + ", ";
        string += "payment commission: " + this.paymentCommission.toString() + ", ";
        string += "sender card id: " + this.senderCardId.toString() + ", ";
        string += "recipient card id: " + this.recipientCardId.toString() + ", ";
        string += "status: " + this.status.toString();
        return string;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    void updateField(String propertyName, String value) {

    }


}
