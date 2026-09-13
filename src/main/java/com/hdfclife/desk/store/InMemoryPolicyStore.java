package com.hdfclife.desk.store;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.hdfclife.desk.exception.DuplicatePolicyException;
import com.hdfclife.desk.exception.PolicyNotFoundException;
import com.hdfclife.desk.model.Policy;

@Repository
public class InMemoryPolicyStore implements PolicyStore {

	private final Map<String, Policy> policies = new LinkedHashMap<>();

	@Override
	public synchronized Policy add(Policy policy) {
		if (policies.containsKey(policy.getPolicyNo())) {
			throw new DuplicatePolicyException("Policy already exists: " + policy.getPolicyNo());
		}
		Policy copy = copyOf(policy);
		policies.put(copy.getPolicyNo(), copy);
		return copyOf(copy);
	}

	@Override
	public synchronized Optional<Policy> findByPolicyNo(String policyNo) {
		Policy policy = policies.get(policyNo);
		return policy == null ? Optional.empty() : Optional.of(copyOf(policy));
	}

	@Override
	public synchronized List<Policy> findAll() {
		return policies.values().stream().map(this::copyOf).collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public synchronized List<Policy> findByStatus(String status) {
		return policies.values().stream()
				.filter(p -> p.getStatus().equals(status))
				.map(this::copyOf)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public synchronized List<Policy> findByType(String type) {
		return policies.values().stream()
				.filter(p -> p.getType().equals(type))
				.map(this::copyOf)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public synchronized List<Policy> findByStatusAndType(String status, String type) {
		return policies.values().stream()
				.filter(p -> p.getStatus().equals(status) && p.getType().equals(type))
				.map(this::copyOf)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public synchronized Policy update(String policyNo, Policy policy) {
		if (!policies.containsKey(policyNo)) {
			throw new PolicyNotFoundException("Policy not found: " + policyNo);
		}
		Policy updated = new Policy(
				policyNo,
				policy.getCustomer(),
				policy.getType(),
				policy.getBasePremium(),
				policy.getStatus());
		policies.put(policyNo, updated);
		return copyOf(updated);
	}

	@Override
	public synchronized void delete(String policyNo) {
		if (policies.remove(policyNo) == null) {
			throw new PolicyNotFoundException("Policy not found: " + policyNo);
		}
	}

	@Override
	public synchronized boolean exists(String policyNo) {
		return policies.containsKey(policyNo);
	}

	@Override
	public synchronized long count() {
		return policies.size();
	}

	@Override
	public synchronized long countByStatus(String status) {
		return policies.values().stream().filter(p -> p.getStatus().equals(status)).count();
	}

	@Override
	public synchronized long countByType(String type) {
		return policies.values().stream().filter(p -> p.getType().equals(type)).count();
	}

	@Override
	public synchronized long uniqueCustomerCount() {
		return policies.values().stream().map(Policy::getCustomer).distinct().count();
	}

	private Policy copyOf(Policy policy) {
		return new Policy(
				policy.getPolicyNo(),
				policy.getCustomer(),
				policy.getType(),
				policy.getBasePremium(),
				policy.getStatus());
	}
}
