package instructions;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;

import common.Constants;
import common.IllegalRegisterException;
import common.NumberTools;

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
	
	public static void populate() {
        map.put("NOP", new ASInstructionSpec("C", 0));
        map.put("ADD", new ASInstructionSpec("A", 1));
        map.put("SUB", new ASInstructionSpec("A", 2));
        map.put("ADDI", new ASInstructionSpec("B", 3));
        map.put("SUBI", new ASInstructionSpec("B", 4));
        map.put("ADDS", new ASInstructionSpec("A", 5));
        map.put("SUBS", new ASInstructionSpec("A", 6));
        map.put("ADDIS", new ASInstructionSpec("B", 7));
        map.put("SUBIS", new ASInstructionSpec("B", 8));
        map.put("AND", new ASInstructionSpec("A", 21));
        map.put("ORR", new ASInstructionSpec("A", 22));
        map.put("EOR", new ASInstructionSpec("A", 23));
        map.put("ANDI", new ASInstructionSpec("B", 24));
        map.put("ORRI", new ASInstructionSpec("B", 25));
        map.put("EORI", new ASInstructionSpec("B", 26));
        map.put("LSL", new ASInstructionSpec("B", 27));
        map.put("LSR", new ASInstructionSpec("B", 28));
        map.put("LDUR", new ASInstructionSpec("D", 9));
        map.put("STUR", new ASInstructionSpec("D", 10));
        map.put("LDURSW", new ASInstructionSpec("D", 11));
        map.put("STURW", new ASInstructionSpec("D", 12));
        map.put("LDURH", new ASInstructionSpec("D", 13));
        map.put("STURH", new ASInstructionSpec("D", 14));
        map.put("LDURB", new ASInstructionSpec("D", 15));
        map.put("STURB", new ASInstructionSpec("D", 16));
        map.put("CBZ", new ASInstructionSpec("E", 29));
        map.put("CBNZ", new ASInstructionSpec("E", 30));
        map.put("B", new ASInstructionSpec("G", 31));
        map.put("BR", new ASInstructionSpec("H", 32));
        map.put("BL", new ASInstructionSpec("G", 33));
        map.put("B.EQ", new ASInstructionSpec("G", 34));
        map.put("B.NE", new ASInstructionSpec("G", 35));
        map.put("B.LT", new ASInstructionSpec("G", 36));
        map.put("B.LE", new ASInstructionSpec("G", 37));
        map.put("B.GT", new ASInstructionSpec("G", 38));
        map.put("B.GE", new ASInstructionSpec("G", 39));
        map.put("B.MI", new ASInstructionSpec("G", 40));
        map.put("B.PL", new ASInstructionSpec("G", 41));
        map.put("B.VS", new ASInstructionSpec("G", 42));
        map.put("B.VC", new ASInstructionSpec("G", 43));
        map.put("PUSH", new ASInstructionSpec("H", 44));
        map.put("POP", new ASInstructionSpec("H", 45));
        map.put("MOVZ", new ASInstructionSpec("E", 46));
        map.put("HALT", new ASInstructionSpec("C", 47));
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
			case "E":
				return Optional.of(new ASInstructionE(instruction, binaryString));
			case "G":
				return Optional.of(new ASInstructionG(instruction, binaryString));
			case "H":
				return Optional.of(new ASInstructionH(instruction, binaryString));
			}
		}
		return Optional.empty();
	}

	public static Optional<AssemblerInstruction> makeInstruction(String binaryString) {
        Optional<String> opcodeName = ASInstructionClassifier.getName((int) NumberTools.binaryStringToNumber(binaryString.substring(0, Constants.OPCODE_LENGTH)));

        if (opcodeName.isPresent()) {
            Optional<AssemblerInstruction> instructionOpt = ASInstructionClassifier.makeInstruction(opcodeName.get(), binaryString);
            return instructionOpt;
        } else {
            return Optional.empty();
        }
    }
}
