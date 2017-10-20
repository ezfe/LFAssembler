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

	public static void main(String[] args) {
		Assembler assembler = new Assembler();
		assembler.run(args);
	}
	
	public void run(String[] args) {
		ASInstructionClassifier.populate("src/ASISpec.txt");
		
		String s = "";
		try {
			  byte[] encoded = Files.readAllBytes(Paths.get("src/Test.txt"));
			  s = new String(encoded, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner lineScanner = new Scanner(s);
		
		AssemblyConfigurations conf = new AssemblyConfigurations();
		
		//TODO
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
					Label l = new Label(token.substring(0, token.length() - 1));
					tokens.add(l);
				} else {
					//OPERATION
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
						System.err.print("Line " + lineNumber + ": ");
						System.err.println(line.trim());
					}
				}
			}
			
			scanner.close();
			lineNumber += 1;
		}
		
		for(Token t: tokens) {
			System.out.println(t.toString());
		}
		
		if (!(conf.maxMemorySet() && conf.registerCountSet() && conf.wordSizeSet())) {
			System.err.println("Configuration is incomplete!");
			System.err.println(conf.toString());
		}
		
		BitSet bitOutput = new BitSet();
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
		
		System.out.println("Locations: " + labelLocations);
		System.out.println("Fill In: " + unfilledLabelReferences);
		
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
		
		try {
			Files.write(Paths.get("src/Out2.txt"), bitOutput.bytes());
		} catch (IOException e) {
			System.err.println("An error occurred writing the output file");
			e.printStackTrace();
		}
		
		lineScanner.close();
	}	
}
