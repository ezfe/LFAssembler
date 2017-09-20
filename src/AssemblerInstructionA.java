import java.util.Optional;
import java.util.Scanner;

/**
 * 
 * @author Ezekiel Elin
 *
 */

public class AssemblerInstructionA extends AssemblerInstruction {
	
	String token = "";
	String p1 = "";
	String p2 = "";
	String p3 = "";
	
	public AssemblerInstructionA(String token, Scanner scanner) {
		this.token = token;
		p1 = scanner.next();
		p2 = scanner.next();
		p3 = scanner.next();
	}
	
	String makeRepresentation() {
		Optional<Integer> opcode = AIOpcodeTransformer.getOpcode(token);
		if (opcode.isPresent()) {
			String opcodeString = String.format("%16s", Integer.toBinaryString(opcode.get())).replace(" ", "0");
			return opcodeString;
		} else {
			return "OPCODE CREATE ERROR";
		}
	}
}
