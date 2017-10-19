package simulator;

import java.util.Optional;

import assembler.ASInstructionClassifier;
import common.BitSet;
import common.Constants;
import common.NumberTools;
import instructions.AssemblerInstruction;

public class Simulator {

	BitSet bits = null;

	public static void main(String[] args) {
		ASInstructionClassifier.populate("src/ASISpec.txt");
		Simulator simulator = new Simulator();
		simulator.run(args);
	}
	
	public void run(String[] args) {
		this.bits = new BitSet("src/Out2.txt");
		
		int index = 0;
		while (true) {
			String instructionString = bits.readInstruction(index);
			String opcodeBinaryString = instructionString.substring(0, Constants.OPCODE_LENGTH);
			Optional<String> opcodeName = ASInstructionClassifier.getName(NumberTools.binaryStringToNumber(opcodeBinaryString));
			
			if (opcodeName.isPresent()) {
				Optional<AssemblerInstruction> instruction = ASInstructionClassifier.makeInstruction(opcodeName.get(), instructionString);
				if (instruction.isPresent()) {
					System.out.println(instruction.get().toString());
				} else {
					System.out.println("Unable to make " + opcodeName.get() + " into an AssemblerInstruction");
				}
			} else {
				System.out.println("Unable to make " + opcodeBinaryString + " into an opcode name");
			}
			
			index += 4;
		}
	}

}
