package assembler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;

import assembler.DirectiveDataContainer.Size;
import common.BitIndex;
import common.BitSet;
import common.Constants;
import common.Directive;
import common.Label;
import common.NumberTools;
import common.Token;
import instructions.ASInstructionClassifier;
import instructions.AssemblerInstruction;

/**
 * The assembler is the entry point for transferring an Assembly source file into machine code.
 * 
 *
 *
 * @author Ezekiel Elin
 *
 */

public class Assembler {

	public static boolean verbose = false;

	public static void main(String[] args) {
		Assembler assembler = new Assembler();
		if (args.length < 2) {
			System.out.println("The assembler must be run with two parameters:");
			System.out.println("assembler [source file path] [output file path]");
			return;
		}
		if (args.length == 3) {
			Assembler.verbose = args[2].equals("true") ? true : false;
		}
		assembler.run(args[0], args[1]);
	}
	
	public void run(String assemblyFilePath, String outputFilePath) {
		ASInstructionClassifier.populate();
		
		String s = "";
		try {
			  byte[] encoded = Files.readAllBytes(Paths.get(assemblyFilePath));
			  s = new String(encoded, StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Unable to find file: " + assemblyFilePath);
			return;
		}
		Scanner lineScanner = new Scanner(s);
		
		ProgramConfiguration conf = new ProgramConfiguration();
		
		HashMap<String, ArrayList<BitIndex>> unfilledLabelReferences = new HashMap<>();
		HashMap<String, Long> labelLocations = new HashMap<>();
		
		Integer lineNumber = 0;
		Boolean error = false;
		ArrayList<Token> tokens = new ArrayList<>();
		while(lineScanner.hasNextLine() && !error) {
			String line = lineScanner.nextLine();
			Scanner scanner = new Scanner(line.split(";")[0]);
			scanner.useDelimiter("(\\p{javaWhitespace}|,|,)+");
			
			while (scanner.hasNext() && !error) {
				String token = scanner.next();
				if (token.charAt(0) == '.') {
					if (Assembler.verbose) System.out.println("Found directive: " + token);
					//DIRECTIVE
					Directive d = new Directive(token.substring(1), scanner);
					switch (d.getToken()) {
					/* Global Configurations */
					case "wordsize":
						conf.setWordSize(d.getValue());
						break;
					case "regcnt":
						conf.setRegisterCount(d.getValue());
						break;
					case "maxmem":
						conf.setMaxMemory(d.getValue());
						break;
					
					/* Memory Objects */
					case "double":
						tokens.add(new DirectiveDataContainer(Size.DOUBLE, d.getValue()));
						break;
					case "single":
						tokens.add(new DirectiveDataContainer(Size.SINGLE, d.getValue()));
						break;
					case "half":
						tokens.add(new DirectiveDataContainer(Size.HALF, d.getValue()));
						break;
					case "byte":
						tokens.add(new DirectiveDataContainer(Size.BYTE, d.getValue()));
						break;
						
					/* Memory Creation Logic */
					case "align":
						tokens.add(new AlignToken(d.getValue()));
						break;
					case "pos":
						tokens.add(new PositionToken(d.getValue()));
						break;
						
					default:
						System.err.println("Unknown directive: " + d.toString());
					}
				} else if (token.charAt(token.length() - 1) == ':') {
					if (Assembler.verbose) System.out.println("Found label " + token);
					Label l = new Label(token.substring(0, token.length() - 1));
					tokens.add(l);
				} else {
					//OPERATION
					if (Assembler.verbose) System.out.println("Found instruction " + token);
					try {
						Optional<AssemblerInstruction> ins = ASInstructionClassifier.makeInstruction(token, scanner);
	
						if (ins.isPresent()) {
							tokens.add(ins.get());
						} else {
							System.err.println(token + " is invalid");
							System.err.println("Line " + lineNumber);
							System.err.println(line.trim());
						}
					} catch (Exception e) {
						System.err.println(e.getLocalizedMessage());
						System.err.print("An error occurred at line " + lineNumber + ": ");
						System.err.println(line.trim());
					}
				}
			}
			
			scanner.close();
			lineNumber += 1;
		}
		
		if (Assembler.verbose) {
			for(Token t: tokens) {
				System.out.println(t.toString());
			}
		}
		
		BitSet bitOutput = new BitSet();
		bitOutput.setMaxByteCount(conf.getMaxMemory());
		for(Token t: tokens) {
			if (t instanceof AssemblerInstruction) {
				AssemblerInstruction ins = (AssemblerInstruction) t;
				if (ins instanceof LabelInstruction) {
					LabelInstruction insLabel = (LabelInstruction) ins;
					if (!unfilledLabelReferences.containsKey(insLabel.getLabel())) {
						unfilledLabelReferences.put(insLabel.getLabel(), new ArrayList<BitIndex>());
					}
					ArrayList<BitIndex> existingIndexes = unfilledLabelReferences.get(insLabel.getLabel());
					
					BitIndex index = new BitIndex(bitOutput.getByteCount(), 31 - insLabel.getLabelBitIndex());
					existingIndexes.add(index);
				}
				String binaryString = ins.binaryStringRepresentation();
				bitOutput.appendStringBlock(binaryString);
			} else if (t instanceof DirectiveDataContainer) {
				DirectiveDataContainer dc = (DirectiveDataContainer) t;
				String binaryString = NumberTools.numberToBinaryString(dc.getValue(), dc.getWidth());
				bitOutput.appendStringBlock(binaryString);
			} else if (t instanceof AlignToken) {
				/* Fill up the last byte to avoid
				 * issues with partially filled bytes
				 */
				bitOutput.byteAlign();
				
				long align = ((AlignToken) t).getAlignment();
				
				/*
				 * Iterations would be required even if we know how many bytes to append,
				 * this makes the code easier to understand.
				 */
				while (bitOutput.getByteCount() % align != 0) {
					bitOutput.appendByte((byte) 0);
				}
			} else if (t instanceof PositionToken) {
				/* Fill up the last byte to avoid
				 * issues with partially filled bytes
				 */
				bitOutput.byteAlign();
				
				long pos = ((PositionToken) t).getPosition();
				long needed = pos - bitOutput.getByteCount();
				for(int i = 0; i < needed; i++) {
					bitOutput.appendByte((byte) 0); 
				}
			} else if (t instanceof Label) {
				String value = ((Label) t).getToken();
				labelLocations.put(value, bitOutput.getNextByteIndex());
			} else {
				System.err.println("Found something: " + t);
			}
		}
		
		if (Assembler.verbose) {
			System.out.println("Locations: " + labelLocations);
			System.out.println("Fill In: " + unfilledLabelReferences);
		}
		
		for(Entry<String, ArrayList<BitIndex>> entry : unfilledLabelReferences.entrySet()) {
			String label = entry.getKey();
			
			if (!labelLocations.containsKey(label)) {
				System.err.println("Unable to fill in label: " + label);
				continue;
			}
			ArrayList<BitIndex> fillThese = entry.getValue();
			
			long location = labelLocations.get(label);
			String locationBinaryString = NumberTools.numberToBinaryString(location, Constants.MEMADDR_LENGTH);
			for(BitIndex index: fillThese) {
				BitIndex p = index;
				for(int i = 0; i < locationBinaryString.length(); i++) {
					char cbit = locationBinaryString.charAt(i);
					bitOutput.setBit(cbit == '0' ? 0 : 1, p);
					p = p.decrement();
				}
			}
		}
		
		int stackLocation = bitOutput.getByteCount();
		if (labelLocations.containsKey("stack")) {
			stackLocation = (int) /* i hate java */ (long) labelLocations.get("stack");
		}
		conf.setStackAddress(stackLocation);

        while (bitOutput.getByteCount() < conf.getMaxMemory()) {
            bitOutput.appendByte((byte) 0);
        }

		if (!(conf.maxMemorySet() && conf.registerCountSet() && conf.wordSizeSet())) {
			System.err.println("Configuration is incomplete!");
			System.out.println(conf.toString());
		}

		System.out.println("Successfully parsed assembly file " + assemblyFilePath + " and wrote memory map to " + outputFilePath);

		bitOutput.configurationBytes = conf.binaryStringRepresentation();
		bitOutput.writeToFile(Paths.get(outputFilePath));

		lineScanner.close();
	}	
}
