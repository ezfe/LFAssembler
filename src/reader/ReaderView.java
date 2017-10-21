package reader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import common.BitSet;
import common.Constants;
import common.NumberTools;
import instructions.ASInstructionClassifier;
import instructions.AssemblerInstruction;

public class ReaderView {

    private BitSet memory = null;
    private Path path = null;
    private boolean attached = false;

    private JTextArea textArea1;
    private JPanel panel1;
    private JTextField a0x0TextField;
    private JButton reloadFromFileButton;
    private JButton readInstructionButton;
    private JLabel readOutLabel;

    public ReaderView(BitSet attachedMemory) {
        this.attached = attachedMemory != null;
        this.memory = attachedMemory;

        reloadFromFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!attached) {
                    if (path == null) {
                        JFileChooser filePicker = new JFileChooser();
                        filePicker.setApproveButtonText("Load Memory Image");
                        int returnValue = filePicker.showOpenDialog(reloadFromFileButton);

                        File file = filePicker.getSelectedFile();
                        path = file.toPath();
                    }
                    memory = new BitSet(path);
                }
                updateMemoryViewport();
            }
        });
        readInstructionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hexString = a0x0TextField.getText();
                long memaddr = Long.parseLong(hexString.substring(2), 16);
                String binaryInstructionString = memory.readBytes((int) memaddr, 4);
                Optional<String> opcodeName = ASInstructionClassifier.getName((int) NumberTools.binaryStringToNumber(binaryInstructionString.substring(0, Constants.OPCODE_LENGTH)));
                if (opcodeName.isPresent()) {
                    Optional<AssemblerInstruction> ins = ASInstructionClassifier.makeInstruction(opcodeName.get(), binaryInstructionString);
                    if (ins.isPresent()) {

                        readOutLabel.setText(ins.get().sourceStringRepresentation());
                    } else {
                        readOutLabel.setText("Unable to classify " + opcodeName.get() + "...");
                    }
                } else {
                    readOutLabel.setText("Unable to get instruction name...");
                }
            }
        });
    }

    /**
     * Fetch the memory string and update the text area
     */
    public void updateMemoryViewport() {
        if (this.memory != null) {
            this.textArea1.setText(this.memory.toByteString());
        }
    }

    public static void show(BitSet attachedMemory) {
        JFrame frame = new JFrame("Viewer");
        frame.setContentPane(new ReaderView(attachedMemory).panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
