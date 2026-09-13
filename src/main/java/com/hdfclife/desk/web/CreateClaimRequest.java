package com.hdfclife.desk.web;

import com.hdfclife.desk.model.Urgency;

public class CreateClaimRequest {

	private String policyNo;
	private int claimAmount;
	private Urgency urgency;

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public int getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(int claimAmount) {
		this.claimAmount = claimAmount;
	}

	public Urgency getUrgency() {
		return urgency;
	}

	public void setUrgency(Urgency urgency) {
		this.urgency = urgency;
	}
}
