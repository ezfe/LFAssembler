package assembler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;

import assembler.DirectiveDataContainer.Size;
import common.ASInstructionClassifier;
import common.AssemblerInstruction;
import common.BitIndex;
import common.BitSet;
import common.BitTools;
import common.Directive;
import common.IllegalRegisterException;
import common.Label;
import common.NumberTools;
import common.Token;
import toolchain.TCTool;

/**
 * The assembler is the entry point for transferring an Assembly source file into machine code.
 * 
 *
 *
 * @author Ezekiel Elin
 *
 */

public class Assembler implements TCTool {

	@Override
	public String run(String[] args) {
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
					} catch (IllegalRegisterException e) {
						System.err.println(e.getLocalizedMessage());
						System.err.println("Line " + lineNumber);
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
			System.out.println(conf.toString());
		}
		
		BitSet bitOutput = new BitSet();
		for(Token t: tokens) {
			if (t instanceof AssemblerInstruction) {
				String binaryString = ((AssemblerInstruction) t).binaryStringRepresentation();
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
				
				int align = ((AlignToken) t).getAlignment();
				
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
				
				int pos = ((PositionToken) t).getPosition();
				int needed = pos - bitOutput.getByteCount();
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
		
		try {
			Files.write(Paths.get("src/Out2.txt"), bitOutput.bytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		lineScanner.close();
		
		return "OK";
	}
	
}
