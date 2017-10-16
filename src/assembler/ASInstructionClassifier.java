package assembler;
import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;

import common.IllegalRegisterException;
import instructions.ASInstructionA;
import instructions.ASInstructionB;
import instructions.ASInstructionC;
import instructions.ASInstructionD;
import instructions.ASInstructionE;
import instructions.ASInstructionG;
import instructions.ASInstructionH;
import instructions.AssemblerInstruction;

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
	
	public static void populate(Scanner sc) {
		while (sc.hasNext()) {
			String instruction = sc.next();
			String type = sc.next();
			Integer opcode = Integer.parseInt(sc.next());
			ASInstructionSpec spec = new ASInstructionSpec(type, opcode);
			map.put(instruction, spec);
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
}
