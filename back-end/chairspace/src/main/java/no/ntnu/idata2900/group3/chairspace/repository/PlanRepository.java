package no.ntnu.idata2900.group3.chairspace.repository;

import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the Plan entity.
 */
public interface PlanRepository extends JpaRepository<Plan, UUID> {}
