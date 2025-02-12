package no.ntnu.idata2900.group3.chairspace.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import no.ntnu.idata2900.group3.chairspace.entity.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, UUID> {}