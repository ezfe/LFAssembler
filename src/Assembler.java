import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import common.ASInstructionClassifier;
import common.AssemblerInstruction;
import common.Directive;
import common.IllegalRegisterException;
import common.Label;
import common.Token;
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
		Scanner lineScanner = new Scanner(s);
		
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
		
		Integer lineNumber = 0;
		Boolean error = false;
		ArrayList<Token> tokens = new ArrayList<>();
		while(lineScanner.hasNextLine() && !error) {
			String line = lineScanner.nextLine();
			Scanner scanner = new Scanner(line.split(";")[0]);
			scanner.useDelimiter("(\\p{javaWhitespace}|,|,)+");
			
			while (scanner.hasNext() && !error) {
				String token = scanner.next();
				if (token.charAt(0) == '.') {
					//DIRECTIVE
					Directive d = new Directive(token.substring(1), scanner);
					tokens.add(d);
				} else if (token.charAt(token.length() - 1) == ':') {
					Label l = new Label(token.substring(0, token.length() - 1));
					tokens.add(l);
				} else {
					//OPERATION
					try {
						Optional<AssemblerInstruction> ins = ASInstructionClassifier.makeInstruction(token, scanner);
	
						if (ins.isPresent()) {
							tokens.add(ins.get());
							System.out.println(ins.get().binaryStringRepresentation());
						} else {
							System.err.println(token + " is invalid");
							System.err.println("Line " + lineNumber);
							System.err.println(line.trim());
						}
					} catch (IllegalRegisterException e) {
						System.err.println(e.getLocalizedMessage());
						System.err.println("Line " + lineNumber);
						System.err.println(line.trim());
					}
				}
			}
			
			lineNumber += 1;
		}
		
		for(Token t: tokens) {
			System.out.println(t.toString());
		}
		
		ArrayList<String> linesOut = new ArrayList<>();
		for(Token t: tokens) {
			if (t instanceof AssemblerInstruction) {
				linesOut.add(((AssemblerInstruction) t).binaryStringRepresentation());
			}
		}
		
		try {
			Files.write(Paths.get("src/Out.txt"), linesOut, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "OK";
	}
	
}
