package simulator;

import javax.swing.*;

public class SimulatorView {
    private JPanel panel1;
    private JTextArea simulatorStateTextArea;
    private JButton stepButton;
    private JSlider simulatorSpeed;
    private JCheckBox autoStepCheckBox;
    private JButton haltButton;
    private JButton reloadButton;

    public static void show() {
        JFrame frame = new JFrame("Simulator");
        frame.setContentPane(new SimulatorView().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
