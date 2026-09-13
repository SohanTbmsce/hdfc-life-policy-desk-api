# HDFC Life Policy Desk API

Spring Boot 3 REST API for HDFC Life policies. Policies and claims live in an in-memory store; Flyway owns the PostgreSQL-ready relational schema.

## How to run

```bash
./mvnw spring-boot:run
```

Or:

```bash
mvn spring-boot:run
```

Default profile is `dev` (H2 in-memory, Flyway enabled).

Swagger UI: **http://localhost:8080/swagger-ui/index.html**  
OpenAPI JSON: **http://localhost:8080/v3/api-docs**

## Endpoints

| Method | Path | Status codes |
|--------|------|--------------|
| GET | `/api/policies` | 200 |
| GET | `/api/policies/{policyNo}` | 200, 404 |
| GET | `/api/policies?status=Active` | 200 |
| GET | `/api/policies?type=TERM` | 200 |
| POST | `/api/policies` | 201, 409 |
| PUT | `/api/policies/{policyNo}` | 200, 404 |
| DELETE | `/api/policies/{policyNo}` | 204, 404 |
| GET | `/api/policies/{policyNo}/claims` | 200, 404 |
| POST | `/api/claims` | 201, 400, 404 |
| GET | `/api/claims/{claimNo}` | 200, 404 |

## Entity-relationship (Flyway schema)

Five tables in 3NF:

- **customers** — `id` PK; `full_name`, `email` unique
- **policies** — `id` PK; `policy_no` unique; `customer_id` → **customers(id)**
- **claims** — `id` PK; `claim_no` unique; `policy_id` → **policies(id)**
- **riders** — `id` PK; `code` unique
- **policy_riders** — composite PK `(policy_id, rider_id)`; `policy_id` → **policies(id)**; `rider_id` → **riders(id)**

## In-memory store vs PostgreSQL (and Flyway vs ddl-auto)

Keep the desk in memory for demos, local labs, and grading where speed and a fixed seed matter more than durability. Move to PostgreSQL when multiple instances, restarts, or audit trails must keep the same policy and claim data. Flyway versions schema as checked-in SQL migrations you can review and apply the same way in every environment. That is stronger than `ddl-auto=update`, which mutates the database from entity mappings without a clear history, is unsafe for production, and is not used here at all — REST data stays in memory while Flyway still proves the relational model.

## Profiles

- **dev** — H2 (`jdbc:h2:mem:hdfclife;MODE=PostgreSQL;DB_CLOSE_DELAY=-1`), H2 console on, `hdfc.company-name=HDFC Life`
- **prod** — PostgreSQL via `DB_URL`, `DB_USER`, `DB_PASSWORD`; `hdfc.company-name=HDFC Life Production`
