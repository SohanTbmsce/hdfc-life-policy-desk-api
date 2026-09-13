package com.hdfclife.desk.web;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hdfclife.desk.model.Claim;
import com.hdfclife.desk.service.ClaimService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/claims")
@Tag(name = "Claims")
public class ClaimController {

	private final ClaimService claimService;

	public ClaimController(ClaimService claimService) {
		this.claimService = claimService;
	}

	@PostMapping
	@Operation(summary = "File a claim")
	@ApiResponse(responseCode = "201", description = "Claim submitted")
	@ApiResponse(responseCode = "400", description = "Invalid claim amount")
	@ApiResponse(responseCode = "404", description = "Policy not found")
	public ResponseEntity<Claim> create(@RequestBody CreateClaimRequest request) {
		Claim created = claimService.fileClaim(
				request.getPolicyNo(),
				request.getClaimAmount(),
				request.getUrgency());
		return ResponseEntity.status(HttpStatus.CREATED)
				.location(URI.create("/api/claims/" + created.getClaimNo()))
				.body(created);
	}

	@GetMapping("/{claimNo}")
	@Operation(summary = "Get claim by number")
	@ApiResponse(responseCode = "200", description = "Claim found")
	@ApiResponse(responseCode = "404", description = "Claim not found")
	public ResponseEntity<Claim> get(@PathVariable String claimNo) {
		return ResponseEntity.ok(claimService.findByClaimNo(claimNo));
	}
}
