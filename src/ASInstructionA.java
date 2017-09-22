import java.util.Optional;
import java.util.Scanner;

/**
 * 
 * @author Ezekiel Elin
 *
 */

public class ASInstructionA extends AssemblerInstruction {
	
	Integer r1 = 0;
	Integer r2 = 0;
	Integer r3 = 0;
	
	public ASInstructionA(String token, Scanner scanner) {
		this.token = token;
		
		String r1String = scanner.next();
		String r2String = scanner.next();
		String r3String = scanner.next();
		
		this.r1 = Integer.parseInt(r1String.substring(1));
		this.r2 = Integer.parseInt(r2String.substring(1));
		this.r3 = Integer.parseInt(r3String.substring(1));
	}
	
	String makeRepresentation() {
		return "" + this.token + " R" + this.r1 + " R" + this.r2 + " R"  + this.r3;

//		Optional<Integer> opcode = ASInstructionClassifier.getOpcode(this.token);
//		if (opcode.isPresent()) {
//			String opcodeString = String.format("%11s", Integer.toBinaryString(opcode.get())).replace(" ", "0");
//			String r1String = String.format("%5s", Integer.toBinaryString(this.r1)).replace(" ", "0");
//			String r2String = String.format("%5s", Integer.toBinaryString(this.r2)).replace(" ", "0");
//			String r3String = String.format("%5s", Integer.toBinaryString(this.r3)).replace(" ", "0");
//			return opcodeString + r1String + r2String + r3String;
//		} else {
//			return "OPCODE CREATE ERROR";
//		}
	}
}
