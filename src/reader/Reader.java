package reader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import common.ASInstructionClassifier;
import common.NumberTools;
import toolchain.TCTool;

public class Reader implements TCTool {

	@Override
	public String run(String[] args) {

		String s = "";
		try {
			  byte[] encoded = Files.readAllBytes(Paths.get("src/Out.txt"));
			  s = new String(encoded, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Scanner lsc = new Scanner(s);
		
		while (lsc.hasNextLine()) {
			String instruction = lsc.nextLine();
//			Scanner scanner = new Scanner(instruction);
			
			String opcodeString = instruction.substring(0, 7);
			Integer opcode = NumberTools.binaryStringToNumber(opcodeString);
			String name = ASInstructionClassifier.getName(opcode).get();
			String type = ASInstructionClassifier.getType(name).get();
			
			System.out.println(type);
			
			System.out.println(instruction.substring(0, 7) + "-" + instruction.substring(7));
			
//			scanner.close();
		}
		
		lsc.close();
		
		return null;
	}

}
