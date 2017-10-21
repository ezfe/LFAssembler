package reader;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import common.BitSet;

public class ReaderGUI extends JFrame {
	private static final long serialVersionUID = 1262268315371446851L;
		
	private BitSet bits;
	private JButton loadButton = null;
	private JScrollPane scroller = null;
	private JTextArea textArea = null;
	
	private JTextField examineField = null;
	private JLabel examineOut = null;
	
	public ReaderGUI() {
		this.createUI();
		this.reloadBitsFromFS();
		this.updateUI();
	}

	private void reloadBitsFromFS() {
		this.bits = new BitSet("src/Out2.txt");
	}
	
	private void updateUI() {
		this.textArea.setText(this.bits.toByteString(0, 0));
		examineField.setText("0x0");
	}
	
	private void createUI() {
		loadButton = new JButton("Reload...");
		loadButton.addActionListener(new ReloadAction());
		
		textArea = new JTextArea(8, 20);
		
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		textArea.setEditable(false);
		
		textArea.setLineWrap(true);
		
		scroller = new JScrollPane(textArea);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		JPanel delimeterFields = new JPanel();
		examineField = new JTextField(8);
		examineOut = new JLabel("");
		delimeterFields.add(examineField, BorderLayout.NORTH);
		delimeterFields.add(examineOut, BorderLayout.SOUTH);
		
		getContentPane().add(delimeterFields, BorderLayout.NORTH);
		getContentPane().add(scroller, BorderLayout.CENTER);	
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
