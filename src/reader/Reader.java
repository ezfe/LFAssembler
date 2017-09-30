package reader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import common.ASInstructionClassifier;
import common.NumberTools;
import toolchain.TCTool;

/**
 * 
 * @author Ezekiel Elin
 *
 */
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
		lsc.useDelimiter("");
		Pattern pattern = Pattern.compile(".{32}");
		
//		System.out.
		
		while (lsc.hasNext(pattern)) {
			String instruction = lsc.next(pattern);
			System.out.println(instruction);
			
			String opcodeString = instruction.substring(0, 7);
			Integer opcode = NumberTools.binaryStringToNumber(opcodeString);
			String name = ASInstructionClassifier.getName(opcode).get();
			String type = ASInstructionClassifier.getType(name).get();
			
			System.out.println(type);
			
			ArrayList<String> splits = new ArrayList<>(); //java lol
			switch (type) {
			case "A":
				splits = NumberTools.splitAt(instruction, 7, 12, 17, 22);
				break;
			case "B":
				splits = NumberTools.splitAt(instruction, 7, 12, 13, 18);
				break;
			case "C":
				splits = NumberTools.splitAt(instruction, 7);
				break;
			case "D":
				splits = NumberTools.splitAt(instruction, 7, 12, 17, 31);
				break;
			}
			
			
			for(String str: splits) { 
				System.out.print(str + "-");
			}
			System.out.println("");
			
//			scanner.close();
		}
		
		System.out.println(lsc.next());
		
		lsc.close();
		
		return null;
	}

}
