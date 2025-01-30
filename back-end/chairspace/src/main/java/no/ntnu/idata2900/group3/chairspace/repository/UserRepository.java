package no.ntnu.idata2900.group3.chairspace.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import no.ntnu.idata2900.group3.chairspace.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {}