package simulator;

import common.Constants;
import common.NumberTools;
import instructions.ASInstructionClassifier;
import instructions.AssemblerInstruction;
import instructions.PerformableInstruction;
import reader.ReaderView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class SimulatorView {
    ReaderView memoryViewer;

    private JPanel panel1;
    private JTextArea simulatorStateTextArea;
    private JButton stepButton;
    private JSlider simulatorSpeed;
    private JCheckBox autoStepCheckBox;
    private JButton haltButton;
    private JButton reloadButton;

    private SimulatorState state;

    public SimulatorView(SimulatorState state) {
        this.state = state;
        this.memoryViewer = ReaderView.show(state.memory);

        haltButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state.isHalted = true;
            }
        });
        reloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
            }
        });
        autoStepCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                while(autoStepCheckBox.isSelected() && !state.isHalted) {
                    step();
                }
            }
        });
        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                step();
            }
        });
    }

    public static SimulatorView show(SimulatorState state) {
        SimulatorView view = new SimulatorView(state);
        JFrame frame = new JFrame("Simulator");
        frame.setContentPane(view.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        return view;
    }

    private void step() {
        if (state.isHalted) {
            System.err.println("Simulator halted...");
            return;
        }

        int currentIndex = this.state.programCounter;
        this.state.programCounter += 4;

        String instructionString = "";
        try {
            instructionString = this.state.memory.readInstruction(currentIndex);
        } catch (IndexOutOfBoundsException exception) {
            System.out.println("Reached end of memory file!");
            state.isHalted = true;
            return;
        }
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

        memoryViewer.updateMemoryViewport();
    }
}
