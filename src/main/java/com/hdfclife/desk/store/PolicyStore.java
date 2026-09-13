package com.hdfclife.desk.store;

import java.util.List;
import java.util.Optional;

import com.hdfclife.desk.model.Policy;

public interface PolicyStore {

	Policy add(Policy policy);

	Optional<Policy> findByPolicyNo(String policyNo);

	List<Policy> findAll();

	List<Policy> findByStatus(String status);

	List<Policy> findByType(String type);

	List<Policy> findByStatusAndType(String status, String type);

	Policy update(String policyNo, Policy policy);

	void delete(String policyNo);

	boolean exists(String policyNo);

	long count();

	long countByStatus(String status);

	long countByType(String type);

	long uniqueCustomerCount();
}
