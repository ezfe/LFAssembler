import java.util.Optional;

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
			String toolString = args[0];
			Optional<Tool> tool = Optional.empty();

			switch (toolString) {
				case "assembler":
					tool = Optional.of(new Assembler());
				default:
					System.out.println(toolString);
			}
			
			if (tool.isPresent()) {
				System.out.println(tool.get().run(args));
			} else {
				System.out.println(toolString + " is not a valid tool.");
			}
		}
	}

}
