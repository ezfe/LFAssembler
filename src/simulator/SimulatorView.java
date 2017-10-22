package simulator;

import instructions.ASInstructionClassifier;
import instructions.AssemblerInstruction;
import instructions.PerformableInstruction;
import reader.ReaderView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

public class SimulatorView {
    ReaderView memoryViewer;

    private JPanel panel1;
    private JTextArea simulatorStateTextArea;
    private JButton stepButton;
    private JSlider simulatorSpeed;
    private JCheckBox autoStepCheckBox;
    private JButton haltButton;
    private JButton saveButton;

    private SimulatorState state;

    private Timer stepTimer;
    private final int TIMER_DELAY = 1;
    private int timerCalls = 0;

    public SimulatorView(SimulatorState state) {
        this.state = state;
        this.memoryViewer = ReaderView.show(state.memory);

        // Create a timer to trigger every 1/20th of a second
        this.stepTimer = new Timer(TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (state.isHalted) {
                    stepTimer.stop();
                    return;
                }

                timerCalls += 1;
                if (autoStepCheckBox.isSelected() && ((timerCalls * TIMER_DELAY) >= simulatorSpeed.getValue() * 1000)) {
                    timerCalls = 0;
                    step();
                    System.out.println("Auto-triggered step");
                }
            }
        });
        this.stepTimer.setInitialDelay(2000);
        this.stepTimer.start();

        haltButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state.isHalted = true;
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser fileChooser = new JFileChooser();
                    if (fileChooser.showSaveDialog(saveButton) == JFileChooser.APPROVE_OPTION) {
                        Files.write(fileChooser.getSelectedFile().toPath(), state.toString().getBytes(), StandardOpenOption.CREATE);
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        });
        autoStepCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (autoStepCheckBox.isSelected()) {
                    stepTimer.start();
                } else {
                    stepTimer.stop();
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

        Optional<AssemblerInstruction> instructionOpt = ASInstructionClassifier.makeInstruction(instructionString);
        if (instructionOpt.isPresent()) {
            AssemblerInstruction instruction = instructionOpt.get();
            if (instruction instanceof PerformableInstruction) {
                ((PerformableInstruction) instruction).perform(this.state);
                System.out.println(this.state);
            }
        } else {
            System.out.println("Unable to make " + instructionString + " into an opcode name");
        }

        memoryViewer.updateMemoryViewport();
        // Without this, the text area sometimes doesn't show the most recent string
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                simulatorStateTextArea.setText(state.toString());
            }
        });
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, BorderLayout.CENTER);
        simulatorStateTextArea = new JTextArea();
        simulatorStateTextArea.setColumns(32);
        Font simulatorStateTextAreaFont = this.$$$getFont$$$("Courier", Font.PLAIN, 12, simulatorStateTextArea.getFont());
        if (simulatorStateTextAreaFont != null) simulatorStateTextArea.setFont(simulatorStateTextAreaFont);
        simulatorStateTextArea.setRows(32);
        simulatorStateTextArea.setText("Not loaded...");
        scrollPane1.setViewportView(simulatorStateTextArea);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        panel1.add(panel2, BorderLayout.NORTH);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        panel2.add(panel3, BorderLayout.EAST);
        stepButton = new JButton();
        stepButton.setText("Step");
        panel3.add(stepButton, BorderLayout.EAST);
        autoStepCheckBox = new JCheckBox();
        autoStepCheckBox.setText("Auto-step");
        panel3.add(autoStepCheckBox, BorderLayout.WEST);
        simulatorSpeed = new JSlider();
        simulatorSpeed.setMajorTickSpacing(1);
        simulatorSpeed.setMaximum(4);
        simulatorSpeed.setMinimum(0);
        simulatorSpeed.setMinorTickSpacing(0);
        simulatorSpeed.setPaintLabels(true);
        simulatorSpeed.setPaintTicks(true);
        simulatorSpeed.setPaintTrack(true);
        simulatorSpeed.setSnapToTicks(true);
        simulatorSpeed.setValue(1);
        panel3.add(simulatorSpeed, BorderLayout.NORTH);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new BorderLayout(0, 0));
        panel2.add(panel4, BorderLayout.WEST);
        haltButton = new JButton();
        haltButton.setText("Halt");
        panel4.add(haltButton, BorderLayout.NORTH);
        saveButton = new JButton();
        saveButton.setText("Save");
        panel4.add(saveButton, BorderLayout.SOUTH);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
