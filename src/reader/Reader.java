package reader;

import instructions.ASInstructionClassifier;

/**
 * Opens the ReaderView
 *
 * @author Ezekiel Elin
 */
public class Reader {
	public static void main(String[] args) {
		ASInstructionClassifier.populate();
		ReaderView.show(null);
	}
}
