package instructions;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;

import common.IllegalRegisterException;

/**
 * 
 * @author Ezekiel Elin
 *
 */
public class ASInstructionClassifier {
	
	private static class ASInstructionSpec {
		public final String type;
		public final Integer opcode;
		
		public ASInstructionSpec(String type, Integer opcode) {
			this.type = type;
			this.opcode = opcode;
		}
	}
	
	private static HashMap<String, ASInstructionSpec> map = new HashMap<>();
	
	public static void populate(String path) {
		File specFile = new File(path);
		Scanner sc;
		try {
			sc = new Scanner(specFile);

			while (sc.hasNext()) {
				String instruction = sc.next();
				String type = sc.next();
				Integer opcode = Integer.parseInt(sc.next());
				ASInstructionSpec spec = new ASInstructionSpec(type, opcode);
				map.put(instruction, spec);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("Unable to populate instruction classifier");
			e.printStackTrace();
		}
	}
	
	private static Optional<ASInstructionSpec> getSpec(String instruction) {
		return Optional.ofNullable(map.get(instruction));
	}
	
	public static Optional<String> getType(String instruction) {
		Optional<ASInstructionSpec> spec = getSpec(instruction);
		if (spec.isPresent()) {
			return Optional.of(spec.get().type);
		} else {
			return Optional.empty();
		}
	}
	
	public static Optional<Integer> getOpcode(String instruction) {
		Optional<ASInstructionSpec> spec = getSpec(instruction);
		if (spec.isPresent()) {
			return Optional.of(spec.get().opcode);
		} else {
			return Optional.empty();
		}
	}
	
	public static Optional<String> getName(Integer opcode) {
		for (HashMap.Entry<String, ASInstructionSpec> pair: map.entrySet()) {
			if (pair.getValue().opcode == opcode) {
				return Optional.of(pair.getKey());
			}
		}
		return Optional.empty();
	}
	
	public static Optional<AssemblerInstruction> makeInstruction(String instruction, Scanner sc) throws IllegalRegisterException {
		Optional<String> type = getType(instruction);
		if (type.isPresent()) {
			switch (type.get()) {
			case "A":
				return Optional.of(new ASInstructionA(instruction, sc));
			case "B":
				return Optional.of(new ASInstructionB(instruction, sc));
			case "C":
				return Optional.of(new ASInstructionC(instruction));
			case "D":
				return Optional.of(new ASInstructionD(instruction, sc));
			case "E":
				return Optional.of(new ASInstructionE(instruction, sc));
			case "G":
				return Optional.of(new ASInstructionG(instruction, sc));
			case "H":
				return Optional.of(new ASInstructionH(instruction, sc));
			}
		}
		return Optional.empty();
	}
	
	public static Optional<AssemblerInstruction> makeInstruction(String instruction, String binaryString) {
		Optional<String> type = getType(instruction);
		if (type.isPresent()) {
			switch (type.get()) {
			case "A":
				return Optional.of(new ASInstructionA(instruction, binaryString));
			case "B":
				return Optional.of(new ASInstructionB(instruction, binaryString));
			case "C":
				return Optional.of(new ASInstructionC(instruction));
			case "D":
				return Optional.of(new ASInstructionD(instruction, binaryString));
//			case "E":
//				return Optional.of(new ASInstructionE(instruction, binaryString));
			case "G":
				return Optional.of(new ASInstructionG(instruction, binaryString));
//			case "H":
//				return Optional.of(new ASInstructionH(instruction, binaryString));
			}
		}
		return Optional.empty();
	}
}
