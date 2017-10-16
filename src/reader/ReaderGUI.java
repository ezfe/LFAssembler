package reader;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import common.BitSet;

public class ReaderGUI extends JFrame {
	private static final long serialVersionUID = 1262268315371446851L;
	
	private BitSet bits;
	private JButton loadButton = null;
	private JTextArea textArea = null;
	
	public ReaderGUI() {
		this.createUI();
		this.reloadBitsFromFS();
		this.updateUI();
	}

	private void reloadBitsFromFS() {
		byte[] bites;
		try {
			bites = Files.readAllBytes(Paths.get("src/Out2.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bites = new byte[0];
		}
		this.bits = new BitSet(bites);
	}
	
	private void updateUI() {
		this.textArea.setText(this.bits.toByteString());
	}
	
	private void createUI() {
		loadButton = new JButton("Reload...");
		loadButton.addActionListener(new ReloadAction());
		
		textArea = new JTextArea(5, 20);
		
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		textArea.setEditable(false);
		
		textArea.setLineWrap(true);
		
		getContentPane().add(textArea, BorderLayout.NORTH);	
		getContentPane().add(loadButton, BorderLayout.SOUTH);

//		GroupLayout layout = new GroupLayout(getContentPane());
//		getContentPane().setLayout(layout);
//		layout.setHorizontalGroup(group);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Machine Code Viewer");
		
		pack();
		setVisible(true);
	}
	
	public class ReloadAction extends MouseAdapter implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			reloadBitsFromFS();
			updateUI();
		}
	}
}
