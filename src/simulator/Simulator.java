package simulator;

import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import assembler.ProgramConfiguration;
import common.BitSet;
import common.Constants;
import common.NumberTools;
import instructions.ASInstructionClassifier;
import instructions.AssemblerInstruction;
import instructions.PerformableInstruction;

public class Simulator {

	long timeout = -1;
	SimulatorState state = null;
	
	public static void main(String[] args) throws InterruptedException {
		ASInstructionClassifier.populate("src/ASISpec.txt");
		Simulator simulator = new Simulator();
		simulator.run(args);
	}
	
	public void run(String[] args) throws InterruptedException {
		BitSet readBits = new BitSet("src/Out2.txt");
		ProgramConfiguration conf = new ProgramConfiguration(readBits.readBytes(0, 4));
		/* Remove the first four bytes */
		readBits.removeConfigurationBytes();
		
		readBits.setMaxByteCount(conf.getMaxMemory());
		this.state = new SimulatorState((int) conf.getRegisterCount(), (int) conf.getWordSize(), readBits);
		this.state.stackRegister.setValue(NumberTools.numberToBinaryString(conf.getStackAddress(), Constants.MEMADDR_LENGTH));
				
		System.out.println(this.state);
		
		long delay = 5;
		while (!this.state.isHalted) {
			step();
			if (timeout < 0) {
				if (true) {//!autostep) {
					System.out.println("Press any key to step, or x to exit");
					Scanner sc = new Scanner(System.in);
					String next = sc.nextLine().trim();
					if (next.equals("x")) {
						this.state.isHalted = true;
					}
				}

			} else if (timeout > 0) {
				TimeUnit.SECONDS.sleep(timeout);
			}
		}
		System.out.println("Terminated...");
	}
	
	private void step() {
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
