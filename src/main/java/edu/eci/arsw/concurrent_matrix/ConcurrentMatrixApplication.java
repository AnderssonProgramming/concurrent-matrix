package edu.eci.arsw.concurrent_matrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class ConcurrentMatrixApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ConcurrentMatrixApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Check if running in test mode
		if (args.length > 0 && "--test".equals(args[0])) {
			System.out.println("Running in test mode - skipping game execution");
			return;
		}
		
		// Start the concurrent matrix game
		Game game = new Game();
		game.run();
	}
}
