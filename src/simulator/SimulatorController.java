package simulator;

import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.nio.file.Path;
import javax.swing.*;

import assembler.ProgramConfiguration;
import common.BitSet;
import common.Constants;
import common.NumberTools;
import instructions.ASInstructionClassifier;
import instructions.AssemblerInstruction;
import instructions.PerformableInstruction;
import reader.ReaderView;
import java.awt.*;

/**
 * Controls the SimulatorView and manages opening up files
 * @author Ezekiel Elin
 */
public class SimulatorController {
	SimulatorView simulatorViewer;
	public static boolean verbose = false;

	long timeout = -1;
	SimulatorState state = null;
	
	public static void main(String[] args) throws InterruptedException {
		if (args.length >= 1) {
			SimulatorController.verbose = args[0].equals("true") ? true : false;
		}
		if (SimulatorController.verbose) System.out.println("Verbose mode on");
		ASInstructionClassifier.populate();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFileChooser filePicker = new JFileChooser();
				filePicker.setApproveButtonText("Load Memory Image");
				if (filePicker.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = filePicker.getSelectedFile();
					SimulatorController simulator = new SimulatorController(file.toPath());
				}
			}
		});
	}

	public SimulatorController(Path path) {
		BitSet readBits = new BitSet(path);

		ProgramConfiguration conf = new ProgramConfiguration(readBits.configurationBytes);
		if (SimulatorController.verbose) System.out.println("Loaded configuration bytes: " + conf.binaryStringRepresentation());
		if (SimulatorController.verbose) System.out.println(conf.toString());
		
		readBits.setMaxByteCount(conf.getMaxMemory());
		this.state = new SimulatorState((int) conf.getRegisterCount(), (int) conf.getWordSize(), readBits);
		this.state.stackRegister.setValue(NumberTools.numberToBinaryString(conf.getStackAddress(), Constants.MEMADDR_LENGTH));

		this.simulatorViewer = SimulatorView.show(state);
	}
}
