package instructions;
import java.util.Scanner;

import common.Constants;
import common.IllegalRegisterException;
import common.NumberTools;
import simulator.SimulatorRegister;
import simulator.SimulatorState;

public class ASInstructionD extends PerformableInstruction {

	/**
	 * Register
	 */
	private int destinationRegisterNumber = 0;
	
	/**
	 * Register
	 */
	private int sourceRegisterNumber = 0;
	
	/**
	 * Numerical literal
	 */
	private int sourceLiteral = 0;
	
	public ASInstructionD(String token, Scanner scanner) throws IllegalRegisterException {
		this.token = token;
		
		String r1String = scanner.next();
		String r2String = scanner.next();
		String r3String = scanner.next();
		
		if (r3String.charAt(0) != '#') {
			throw new IllegalArgumentException("r3 must be constant");
		}
		
		this.destinationRegisterNumber = (int) NumberTools.parseNumber(r1String.substring(1));
		this.sourceRegisterNumber = (int) NumberTools.parseNumber(r2String.substring(2));
		this.sourceLiteral = (int) NumberTools.parseNumber(r3String.substring(1, r3String.length() - 1));
		
		AssemblerInstruction.checkRegister(destinationRegisterNumber);
		AssemblerInstruction.checkRegister(sourceRegisterNumber);
	}
	
	public ASInstructionD(String token, String binaryRepresentation) {
		this.token = token;
		
		int start = Constants.OPCODE_LENGTH;
		int end = start + Constants.REGISTER_LENGTH;
		this.destinationRegisterNumber = NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));
		
		start = end;
		end += Constants.REGISTER_LENGTH;
		this.sourceRegisterNumber = NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));

		start = end;
		end += Constants.LITERAL_LENGTH;
		this.sourceLiteral = NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));
	}
	
	@Override
	public String sourceStringRepresentation() {
		return "" + this.token + " R" + this.destinationRegisterNumber + " [R" + this.sourceRegisterNumber + " #"  + this.sourceLiteral + "]";
	}

	@Override
	public String binaryStringRepresentation() {
		String r1String = NumberTools.numberToBinaryString(this.destinationRegisterNumber, Constants.REGISTER_LENGTH);
		String r2String = NumberTools.numberToBinaryString(this.sourceRegisterNumber, Constants.REGISTER_LENGTH);
		String r3String = NumberTools.numberToBinaryString(this.sourceLiteral, Constants.LITERAL_LENGTH);
		
		String instruction = this.opcodeBinaryString() + r1String + r2String + r3String;
		return NumberTools.rpad(instruction, '0', Constants.INSTRUCTION_LENGTH);
	}
	
	@Override
	public void perform(SimulatorState state) {
		super.perform(state);
		SimulatorRegister destinationRegister = state.getRegister(this.destinationRegisterNumber);
		SimulatorRegister sourceRegister = state.getRegister(this.sourceRegisterNumber);
		
		if (token.equals("LDUR")) {
			long memaddr = sourceRegister.getIntValue() + this.sourceLiteral;
			String found = state.memory.readBytes((int) memaddr, 8);
			destinationRegister.setValue(found);
//		} else if (token.equals("STUR")) {
			//TODO
		} else if (token.equals("LDURSW")) {
			long memaddr = sourceRegister.getIntValue() + this.sourceLiteral;
			String found = state.memory.readBytes((int) memaddr, 4);
			destinationRegister.setValue(found);
//		} else if (token.equals("STURW")) {
			//TODO
		} else if (token.equals("LDURH")) {
			long memaddr = sourceRegister.getIntValue() + this.sourceLiteral;
			String found = state.memory.readBytes((int) memaddr, 2);
			destinationRegister.setValue(found);
//		} else if (token.equals("STURH")) {
			//TODO
		} else if (token.equals("LDURB")) {
			long memaddr = sourceRegister.getIntValue() + this.sourceLiteral;
			String found = state.memory.readBytes((int) memaddr, 1);
			destinationRegister.setValue(found);
//		} else if (token.equals("STURB")) {
			//TODO
		} else {
			System.out.println(this.token + " is unimplemented");
		}
	}
}
