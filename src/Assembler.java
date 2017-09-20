import java.util.Optional;
import java.util.Scanner;
/**
 * 
 * @author Ezekiel Elin
 *
 */
public class Assembler implements Tool {

	@Override
	public String run(String[] args) {
		String s = "ADD x0, x1, x2\nSUBI x1, x0, #3";
		Scanner scanner = new Scanner(s);
		
		/* This was taken from https://stackoverflow.com/a/36884167/2059595 */
		scanner.useDelimiter("(\\p{javaWhitespace}|\\.|,)+");
		
		while(scanner.hasNext()) {
			String token = scanner.next();
			System.out.println("Token: " + token);
			Optional<AssemblerInstruction> ins = AssemblerInstruction.makeInstruction(token, scanner);
			
			if (ins.isPresent()) {
				System.out.println(ins.get().makeRepresentation());
			} else {
				System.out.println(token + " is invalid");
			}
		}
		
		return "OK";
	}
	
}
