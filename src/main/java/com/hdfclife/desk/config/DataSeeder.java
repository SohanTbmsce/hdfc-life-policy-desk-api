package com.hdfclife.desk.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import com.hdfclife.desk.model.Policy;
import com.hdfclife.desk.service.PolicyService;
import com.hdfclife.desk.store.PolicyStore;

@Component
public class DataSeeder implements CommandLineRunner {

	private final PolicyStore policyStore;
	private final PolicyService policyService;
	private final HdfcProperties hdfcProperties;
	private final Environment environment;

	public DataSeeder(
			PolicyStore policyStore,
			PolicyService policyService,
			HdfcProperties hdfcProperties,
			Environment environment) {
		this.policyStore = policyStore;
		this.policyService = policyService;
		this.hdfcProperties = hdfcProperties;
		this.environment = environment;
	}

	@Override
	public void run(String... args) {
		if (policyStore.count() == 0) {
			policyStore.add(new Policy("HDFC-LIFE-1001", "Anita Sharma", "TERM", 18500, "Active"));
			policyStore.add(new Policy("HDFC-LIFE-1002", "Rahul Mehta", "ULIP", 42000, "Active"));
			policyStore.add(new Policy("HDFC-LIFE-1003", "Priya Nair", "ENDOWMENT", 27000, "Lapsed"));
			policyStore.add(new Policy("HDFC-LIFE-1004", "Vikram Singh", "TERM", 15200, "Active"));
			policyStore.add(new Policy("HDFC-LIFE-1005", "Sneha Patel", "ULIP", 36000, "Active"));
			policyStore.add(new Policy("HDFC-LIFE-1006", "Anita Sharma", "ENDOWMENT", 22000, "Pending"));
		}

		String activeProfile = environment.getActiveProfiles().length > 0
				? environment.getActiveProfiles()[0]
				: environment.getDefaultProfiles()[0];

		System.out.println("Active profile → " + activeProfile);
		System.out.println("Company name from HdfcProperties → " + hdfcProperties.getCompanyName());
		System.out.println("Max claim amount → " + hdfcProperties.getMaxClaimAmount());
		System.out.println("Seeded policy count → " + policyStore.count());
		System.out.println("Lookup HDFC-LIFE-1004 customer → "
				+ policyStore.findByPolicyNo("HDFC-LIFE-1004").map(Policy::getCustomer).orElse(""));
		System.out.println("Active policy count via PolicyService → " + policyService.countActive());
		System.out.println("TERM policy count via PolicyService → " + policyService.countByType("TERM"));
		System.out.println("Unique customer count → " + policyService.uniqueCustomerCount());
		System.out.println("Simple class name of the injected PolicyStore → "
				+ ClassUtils.getUserClass(policyStore).getSimpleName());
	}
}
