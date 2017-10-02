package reader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import common.ASInstructionClassifier;
import common.BitSet;
import common.NumberTools;
import toolchain.TCTool;

/**
 * 
 * @author Ezekiel Elin
 *
 */
public class Reader implements TCTool {

	@Override
	public String run(String[] args) {		
		
		byte[] bites;
		try {
			bites = Files.readAllBytes(Paths.get("src/Out2.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bites = new byte[0];
		}
		
		BitSet bits = new BitSet(bites);
		
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ReaderGUI(bits);
			}
		});

		
		return null;
	}

}
