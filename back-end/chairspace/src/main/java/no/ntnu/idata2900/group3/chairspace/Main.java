package no.ntnu.idata2900.group3.chairspace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for the application.
 */
@SpringBootApplication
public class Main {

	/**
	 * Main method for the application.
	 *
	 * @param args The arguments for the application.
	 */
	public static void main(String[] args) {
		// I realize that the javadoc is redundant. But the lint is bullying me.
		// If you read the javadoc, and you feel like you need it. Then don't touch my code.
		SpringApplication.run(Main.class, args);
	}

}
