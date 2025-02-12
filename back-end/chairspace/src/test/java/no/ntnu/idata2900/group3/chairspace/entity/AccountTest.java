package no.ntnu.idata2900.group3.chairspace.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Tests for the User entity.
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AccountTest {

	private Account account;

	/**
	 * Setup method for the tests.
	 */
	@BeforeEach
	public void setUp() {
		this.account = new Account(
			"John",
			"Test",
			"JohnTes@ntnu.no",
			12345678
		);
	}

	@DisplayName("Test Creation of user")
	@Test
	public void testCreation() {
		assertNotNull(account);
	}
}
