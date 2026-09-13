package com.hdfclife.desk.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hdfclife.desk.exception.PolicyNotFoundException;
import com.hdfclife.desk.model.Policy;
import com.hdfclife.desk.store.PolicyStore;

@Service
public class PolicyService {

	private final PolicyStore policyStore;

	public PolicyService(PolicyStore policyStore) {
		this.policyStore = policyStore;
	}

	public List<Policy> findAll() {
		return policyStore.findAll();
	}

	public List<Policy> findFiltered(String status, String type) {
		if (status != null && type != null) {
			return policyStore.findByStatusAndType(status, type);
		}
		if (status != null) {
			return policyStore.findByStatus(status);
		}
		if (type != null) {
			return policyStore.findByType(type);
		}
		return policyStore.findAll();
	}

	public Policy findByPolicyNo(String policyNo) {
		return policyStore.findByPolicyNo(policyNo)
				.orElseThrow(() -> new PolicyNotFoundException("Policy not found: " + policyNo));
	}

	public Policy create(Policy policy) {
		return policyStore.add(policy);
	}

	public Policy update(String policyNo, Policy policy) {
		return policyStore.update(policyNo, policy);
	}

	public void delete(String policyNo) {
		policyStore.delete(policyNo);
	}

	public long countActive() {
		return policyStore.countByStatus("Active");
	}

	public long countByType(String type) {
		return policyStore.countByType(type);
	}

	public long uniqueCustomerCount() {
		return policyStore.uniqueCustomerCount();
	}

	public boolean exists(String policyNo) {
		return policyStore.exists(policyNo);
	}
}
