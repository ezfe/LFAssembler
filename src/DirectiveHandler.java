import java.util.Scanner;

/**
 * 
 * @author Ezekiel Elin
 *
 */
public class DirectiveHandler {
	public static void process(String token, Scanner scanner) {
		// single-parameter harcode right now
		String next = scanner.next();
		
		System.out.println("."  + token + " " + next);
		
		return;
	}
}
