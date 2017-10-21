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
		
		Simulator simulator = new Simulator("src/Out2.txt");
		simulator.run(args);
	}
	
	public Simulator(String path) {
		BitSet readBits = new BitSet(path);

		ProgramConfiguration conf = new ProgramConfiguration(readBits.configurationBytes);
		
		readBits.setMaxByteCount(conf.getMaxMemory());
		this.state = new SimulatorState((int) conf.getRegisterCount(), (int) conf.getWordSize(), readBits);
		this.state.stackRegister.setValue(NumberTools.numberToBinaryString(conf.getStackAddress(), Constants.MEMADDR_LENGTH));
	}
	
	public void run(String[] args) throws InterruptedException {
				
		System.out.println(this.state);
		Scanner sc = new Scanner(System.in);
		
		while (!this.state.isHalted) {
			if (timeout < 0) {
				System.out.println("Press any key to step, or x to exit");
				if (sc.hasNextLine()) {
					String next = sc.nextLine().trim();
					if (next.equals("x")) {
						System.out.println("Manual termination");
						break;
					}
				}
			} else if (timeout > 0) {
				TimeUnit.SECONDS.sleep(timeout);
			}
			step();
		}

		sc.close();
		System.out.println("Finished execution...");
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
