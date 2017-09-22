import java.util.Scanner;

/**
 * 
 * @author Ezekiel Elin
 *
 */
public class ASInstructionB extends AssemblerInstruction {

	Integer r1 = 0;
	Integer r2 = 0;
	Integer r3 = 0;
	
	Boolean r2Constant = false;
	Boolean r3Constant = false;
	
	public ASInstructionB(String token, Scanner scanner) {
		this.token = token;
		
		String r1String = scanner.next();
		String r2String = scanner.next();
		String r3String = scanner.next();
		
		r2Constant = (r2String.charAt(0) == '#');
		r3Constant = (r3String.charAt(0) == '#');
		
		this.r1 = Integer.parseInt(r1String.substring(1));
		this.r2 = Integer.parseInt(r2String.substring(1));
		this.r3 = Integer.parseInt(r3String.substring(1));
	}
	
	@Override
	String makeRepresentation() {
		return "" + this.token + " R" + this.r1 + " " + (this.r2Constant ? "#" : "R") + this.r2 + " "  + (this.r3Constant ? "#" : "R") + this.r3;
	}

}
