package reader;

/**
 * 
 * @author Ezekiel Elin
 *
 */
public class Reader {
	public static void main(String[] args) {		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ReaderGUI();
			}
		});
	}

}
