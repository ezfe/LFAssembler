import java.util.Optional;
import java.util.Scanner;

/**
 * 
 * @author Ezekiel Elin
 *
 */

public abstract class AssemblerInstruction {
	public static Optional<AssemblerInstruction> makeInstruction(String token, Scanner scanner) {
		
		switch (token.toLowerCase()) {
		case "add":
			return Optional.of(new AssemblerInstructionA(token, scanner));
		case "sub":
			return Optional.of(new AssemblerInstructionA(token, scanner));
		case "subi":
			return Optional.of(new AssemblerInstructionA(token, scanner));
		}
				
		return Optional.empty();
	}
	
	abstract String makeRepresentation();
}
