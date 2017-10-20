package simulator;

import java.util.Optional;

import common.BitSet;
import common.Constants;
import common.NumberTools;
import instructions.ASInstructionA;
import instructions.ASInstructionClassifier;
import instructions.AssemblerInstruction;
import instructions.PerformableInstruction;

public class Simulator {

	SimulatorState state = null;
	
	public static void main(String[] args) {
		ASInstructionClassifier.populate("src/ASISpec.txt");
		Simulator simulator = new Simulator();
		simulator.run(args);
	}
	
	public void run(String[] args) {
		this.state = new SimulatorState(4, 32, "src/Out2.txt");
		
		SimulatorRegister r0 = this.state.getRegister(0);
		SimulatorRegister r1 = this.state.getRegister(1);
		SimulatorRegister r2 = this.state.getRegister(2);
		
//		r1.setValue(NumberTools.numberToBinaryString(8, 32));
//		r2.setValue(NumberTools.numberToBinaryString(7, 32));
		
		System.out.println(this.state);
		
		int index = 0;
		while (!this.state.isHalted) {
			String instructionString = this.state.memory.readInstruction(index);
			String opcodeBinaryString = instructionString.substring(0, Constants.OPCODE_LENGTH);
			Optional<String> opcodeName = ASInstructionClassifier.getName(NumberTools.binaryStringToNumber(opcodeBinaryString));
			
			if (opcodeName.isPresent()) {
				Optional<AssemblerInstruction> instructionOpt = ASInstructionClassifier.makeInstruction(opcodeName.get(), instructionString);
				if (instructionOpt.isPresent()) {
					AssemblerInstruction instruction = instructionOpt.get();
					if (instruction instanceof PerformableInstruction) {
						((PerformableInstruction) instruction).perform(this.state);
						System.out.println(this.state);
					}
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
