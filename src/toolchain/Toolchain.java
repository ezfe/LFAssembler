package toolchain;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;

import assembler.Assembler;
import common.ASInstructionClassifier;
import reader.Reader;

/**
 * The entry class for the entire project
 * @author Ezekiel Elin
 *
 */
public class Toolchain {
	
	public static void main(String[] args) {
		if (args.length == 0 || args[0].equals("--help")) {
			System.out.println("This is the help thing");
			System.out.println("Format: ./toolchain [tool] (params...)");
		} else {
			File spec = new File("src/ASISpec.txt");
			Scanner specScanner;
			try {
				specScanner = new Scanner(spec);
				ASInstructionClassifier.populate(specScanner);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String toolString = args[0];
			Optional<TCTool> tool = Optional.empty();

			switch (toolString) {
			case "assembler":
				tool = Optional.of(new Assembler());
				break;
			case "reader":
				tool = Optional.of(new Reader());
				break;
			default:
				System.out.println(toolString);
				break;
			}
			
			if (tool.isPresent()) {
				System.out.println(tool.get().run(args));
			} else {
				System.out.println(toolString + " is not a valid tool.");
			}
		}
	}

}
