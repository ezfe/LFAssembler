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
import reader.ReaderView;

public class SimulatorController {
    SimulatorView simulatorViewer;

	long timeout = -1;
	SimulatorState state = null;
	
	public static void main(String[] args) throws InterruptedException {
		ASInstructionClassifier.populate("src/ASISpec.txt");

		SimulatorController simulator = new SimulatorController("src/Out2.txt");
	}

	public SimulatorController(String path) {
		BitSet readBits = new BitSet(path);

		ProgramConfiguration conf = new ProgramConfiguration(readBits.configurationBytes);
		
		readBits.setMaxByteCount(conf.getMaxMemory());
		this.state = new SimulatorState((int) conf.getRegisterCount(), (int) conf.getWordSize(), readBits);
		this.state.stackRegister.setValue(NumberTools.numberToBinaryString(conf.getStackAddress(), Constants.MEMADDR_LENGTH));

        this.simulatorViewer = SimulatorView.show(state);
	}
}
