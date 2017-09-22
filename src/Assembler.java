import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
		String s = "";
		try {
			  byte[] encoded = Files.readAllBytes(Paths.get("src/Test.txt"));
			  s = new String(encoded, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner scanner = new Scanner(s);
		
		File spec = new File("src/ASISpec.txt");
		Scanner specScanner;
		try {
			specScanner = new Scanner(spec);
			ASInstructionClassifier.populate(specScanner);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/* This was taken from https://stackoverflow.com/a/36884167/2059595 */
		scanner.useDelimiter("(\\p{javaWhitespace}|,|,)+");
		
		while(scanner.hasNext()) {
			String token = scanner.next();
			System.out.println("Token: " + token);
			if (token.charAt(0) == '.') {
				//DIRECTIVE
				DirectiveHandler.process(token.substring(1), scanner);
			} else if (token.charAt(token.length() - 1) == ':') {
				//LABEL
				System.out.println("LABEL: " + token);
			} else {
				//OPERATION
				Optional<AssemblerInstruction> ins = ASInstructionClassifier.makeInstruction(token, scanner);
//				
				if (ins.isPresent()) {
					System.out.println(ins.get().makeRepresentation());
				} else {
					System.out.println(token + " is invalid");
				}
			}
		}
		
		return "OK";
	}
	
}
