package com.hdfclife.desk.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hdfclife.desk.model.Claim;
import com.hdfclife.desk.model.Policy;
import com.hdfclife.desk.service.ClaimService;
import com.hdfclife.desk.service.PolicyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/policies")
@Tag(name = "Policies")
public class PolicyController {

	private final PolicyService policyService;
	private final ClaimService claimService;

	public PolicyController(PolicyService policyService, ClaimService claimService) {
		this.policyService = policyService;
		this.claimService = claimService;
	}

	@GetMapping
	@Operation(summary = "List policies", description = "Returns all policies, optionally filtered by status and/or type")
	@ApiResponse(responseCode = "200", description = "Policies returned")
	public ResponseEntity<List<Policy>> list(
			@Parameter(description = "Exact policy status filter") @RequestParam(required = false) String status,
			@Parameter(description = "Exact product type filter") @RequestParam(required = false) String type) {
		return ResponseEntity.ok(policyService.findFiltered(status, type));
	}

	@GetMapping("/{policyNo}")
	@Operation(summary = "Get policy by number")
	@ApiResponse(responseCode = "200", description = "Policy found")
	@ApiResponse(responseCode = "404", description = "Policy not found")
	public ResponseEntity<Policy> get(@PathVariable String policyNo) {
		return ResponseEntity.ok(policyService.findByPolicyNo(policyNo));
	}

	@PostMapping
	@Operation(summary = "Create policy")
	@ApiResponse(responseCode = "201", description = "Policy created")
	@ApiResponse(responseCode = "409", description = "Duplicate policy number")
	public ResponseEntity<Policy> create(@RequestBody Policy policy) {
		Policy created = policyService.create(policy);
		return ResponseEntity.status(HttpStatus.CREATED)
				.location(URI.create("/api/policies/" + created.getPolicyNo()))
				.body(created);
	}

	@PutMapping("/{policyNo}")
	@Operation(summary = "Replace policy fields")
	@ApiResponse(responseCode = "200", description = "Policy updated")
	@ApiResponse(responseCode = "404", description = "Policy not found")
	public ResponseEntity<Policy> update(@PathVariable String policyNo, @RequestBody Policy policy) {
		return ResponseEntity.ok(policyService.update(policyNo, policy));
	}

	@DeleteMapping("/{policyNo}")
	@Operation(summary = "Delete policy")
	@ApiResponse(responseCode = "204", description = "Policy deleted")
	@ApiResponse(responseCode = "404", description = "Policy not found")
	public ResponseEntity<Void> delete(@PathVariable String policyNo) {
		policyService.delete(policyNo);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{policyNo}/claims")
	@Operation(summary = "List claims for a policy", description = "Oldest first")
	@ApiResponse(responseCode = "200", description = "Claims returned")
	@ApiResponse(responseCode = "404", description = "Policy not found")
	public ResponseEntity<List<Claim>> claimsForPolicy(@PathVariable String policyNo) {
		return ResponseEntity.ok(claimService.findByPolicyNo(policyNo));
	}
}
