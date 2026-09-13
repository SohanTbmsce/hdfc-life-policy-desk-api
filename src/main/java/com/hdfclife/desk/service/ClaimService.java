package com.hdfclife.desk.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hdfclife.desk.config.HdfcProperties;
import com.hdfclife.desk.exception.ClaimNotFoundException;
import com.hdfclife.desk.exception.InvalidClaimException;
import com.hdfclife.desk.exception.PolicyNotFoundException;
import com.hdfclife.desk.model.Claim;
import com.hdfclife.desk.model.Urgency;
import com.hdfclife.desk.store.PolicyStore;

@Service
public class ClaimService {

	private final PolicyStore policyStore;
	private final HdfcProperties hdfcProperties;
	private final Map<String, Claim> claims = new LinkedHashMap<>();
	private final AtomicInteger claimSequence = new AtomicInteger(0);

	public ClaimService(PolicyStore policyStore, HdfcProperties hdfcProperties) {
		this.policyStore = policyStore;
		this.hdfcProperties = hdfcProperties;
	}

	public Claim fileClaim(String policyNo, int claimAmount, Urgency urgency) {
		if (!policyStore.exists(policyNo)) {
			throw new PolicyNotFoundException("Policy not found: " + policyNo);
		}
		if (claimAmount <= 0 || claimAmount > hdfcProperties.getMaxClaimAmount()) {
			throw new InvalidClaimException(
					"Claim amount must be greater than 0 and at most " + hdfcProperties.getMaxClaimAmount());
		}
		String claimNo = String.format("CLM-%02d", claimSequence.incrementAndGet());
		Claim claim = new Claim(claimNo, policyNo, claimAmount, urgency, "SUBMITTED");
		claims.put(claimNo, claim);
		return copyOf(claim);
	}

	public Claim findByClaimNo(String claimNo) {
		Claim claim = claims.get(claimNo);
		if (claim == null) {
			throw new ClaimNotFoundException("Claim not found: " + claimNo);
		}
		return copyOf(claim);
	}

	public List<Claim> findByPolicyNo(String policyNo) {
		if (!policyStore.exists(policyNo)) {
			throw new PolicyNotFoundException("Policy not found: " + policyNo);
		}
		return claims.values().stream()
				.filter(c -> c.getPolicyNo().equals(policyNo))
				.map(this::copyOf)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	private Claim copyOf(Claim claim) {
		return new Claim(
				claim.getClaimNo(),
				claim.getPolicyNo(),
				claim.getClaimAmount(),
				claim.getUrgency(),
				claim.getStatus());
	}
}
