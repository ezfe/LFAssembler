package reader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import common.BitSet;

public class ReaderController {

    private BitSet memory = null;

    private JTextArea textArea1;
    private JPanel panel1;
    private JTextField a0x0TextField;
    private JButton reloadFromFileButton;
    private JButton readInstructionButton;

    public ReaderController() {
        reloadFromFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser filePicker = new JFileChooser();
                filePicker.setApproveButtonText("Load Memory Image");
                int returnValue = filePicker.showOpenDialog(reloadFromFileButton);
                File file = filePicker.getSelectedFile()
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Viewer");
        frame.setContentPane(new ReaderController().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
