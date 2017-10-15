package reader;

import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import common.BitSet;

public class ReaderGUI extends JFrame {
	private static final long serialVersionUID = 1262268315371446851L;
	
	private BitSet bits;
	
	public ReaderGUI(BitSet bits) {
		this.bits = bits;
		this.createUI();
	}

	private void createUI() {
		JButton loadButton = new JButton("Load file...");
		
		getContentPane().add(loadButton);
		
		JTextArea textArea = new JTextArea(5, 20);
		
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		JScrollPane scrollPane = new JScrollPane(textArea); 
		textArea.setEditable(false);
		
		textArea.append(this.bits.toByteString());
		textArea.setLineWrap(true);
		
		getContentPane().add(textArea);	
		
//		GroupLayout layout = new GroupLayout(getContentPane());
//		getContentPane().setLayout(layout);
//		layout.setHorizontalGroup(group);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Machine Code Viewer");
		
		pack();
		setVisible(true);
	}
}
