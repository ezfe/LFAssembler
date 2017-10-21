package simulator;

import java.util.Optional;

import assembler.ProgramConfiguration;
import common.BitSet;
import common.Constants;
import common.NumberTools;
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
		BitSet readBits = new BitSet("src/Out2.txt");
		ProgramConfiguration conf = new ProgramConfiguration(readBits.readBytes(0, 4));
		/* Remove the first four bytes */
		readBits.removeConfigurationBytes();
		
		readBits.setMaxByteCount(conf.getMaxMemory());
		this.state = new SimulatorState((int) conf.getRegisterCount(), (int) conf.getWordSize(), readBits);
		this.state.stackRegister.setValue(NumberTools.numberToBinaryString(conf.getStackAddress(), Constants.MEMADDR_LENGTH));
				
		System.out.println(this.state);
		
		while (!this.state.isHalted) {
			int currentIndex = this.state.programCounter;
			this.state.programCounter += 4;
			
			String instructionString = this.state.memory.readInstruction(currentIndex);
			String opcodeBinaryString = instructionString.substring(0, Constants.OPCODE_LENGTH);
			Optional<String> opcodeName = ASInstructionClassifier.getName((int) NumberTools.binaryStringToNumber(opcodeBinaryString));
			
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
		}
	}

}
