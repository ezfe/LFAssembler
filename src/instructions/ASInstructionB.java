package instructions;
import java.util.Scanner;

import common.Constants;
import common.IllegalRegisterException;
import common.NumberTools;
import simulator.BinaryOperations;
import simulator.BinaryOperationsResult;
import simulator.SimulatorRegister;
import simulator.SimulatorState;

/**
 * 
 * @author Ezekiel Elin
 *
 */
public class ASInstructionB extends PerformableInstruction {

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
	
	/**
	 * Indicates whether the order of the constants has been swapped
	 */
	private boolean constantOrderFlipped = false;
	
	public ASInstructionB(String token, Scanner scanner) throws IllegalRegisterException {
		this.token = token;
		
		String arg1String = scanner.next();
		String arg2String = scanner.next();
		String arg3String = scanner.next();
		
		boolean arg2Constant = (arg2String.charAt(0) == '#');
		boolean arg3Constant = (arg3String.charAt(0) == '#');
		
		if ((arg2Constant && arg3Constant) || !(arg2Constant || arg3Constant)) {
			throw new IllegalArgumentException("Cannot have two or zero constants");
		}
		
		// If r2 is a constant, then set the flag
		this.constantOrderFlipped = arg2Constant;
		
		this.destinationRegisterNumber = (int) NumberTools.parseNumber(arg1String.substring(1));
		if (constantOrderFlipped) {			
			//Store arg3 in the register space
			this.sourceRegisterNumber = (int) NumberTools.parseNumber(arg3String.substring(1));
			
			//Store arg2 in the constant space
			this.sourceLiteral = (int) NumberTools.parseNumber(arg2String.substring(1));

		} else {
			//Store arg2 in the register space
			this.sourceRegisterNumber = (int) NumberTools.parseNumber(arg2String.substring(1));
			
			//Store arg3 in the constant space
			this.sourceLiteral = (int) NumberTools.parseNumber(arg3String.substring(1));
		}
		
		AssemblerInstruction.checkRegister(destinationRegisterNumber);
		if (!arg2Constant) AssemblerInstruction.checkRegister(sourceRegisterNumber);
		if (!arg3Constant) AssemblerInstruction.checkRegister(sourceLiteral);
	}
	
	public ASInstructionB(String token, String binaryRepresentation) {
		this.token = token;
		
		int start = Constants.OPCODE_LENGTH;
		int end = start + Constants.REGISTER_LENGTH;
		this.destinationRegisterNumber = NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));
		
		start = end;
		end += 1;
		this.constantOrderFlipped = binaryRepresentation.substring(start, end).equals("1") ? true : false;
		
		start = end;
		end += Constants.REGISTER_LENGTH;
		this.sourceRegisterNumber = NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));

		start = end;
		end += Constants.LITERAL_LENGTH;
		this.sourceLiteral = NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));
	}
	
	@Override
	public String sourceStringRepresentation() {
		return "" + this.token + " R" + this.destinationRegisterNumber + " " + (this.constantOrderFlipped ? ("#" + this.sourceLiteral) : ("R" + this.sourceRegisterNumber)) + " "  +(this.constantOrderFlipped ?  ("R" + this.sourceRegisterNumber) : ("#" + this.sourceLiteral));
	}

	@Override
	public String binaryStringRepresentation() throws IllegalStateException {
		String r1String = NumberTools.numberToBinaryString(this.destinationRegisterNumber, Constants.REGISTER_LENGTH);
		String r2String = NumberTools.numberToBinaryString(this.sourceRegisterNumber, Constants.REGISTER_LENGTH);
		String r3String = NumberTools.numberToBinaryString(this.sourceLiteral, Constants.LITERAL_LENGTH);
		
		String instruction = this.opcodeBinaryString() + r1String + (this.constantOrderFlipped ? "1" : "0") + r2String + r3String;
		return NumberTools.rpad(instruction, '0', Constants.INSTRUCTION_LENGTH);
	}

	@Override
	public void perform(SimulatorState state) {
		super.perform(state);
		SimulatorRegister destinationRegister = state.getRegister(this.destinationRegisterNumber);
		SimulatorRegister leftSourceRegister = state.getRegister(this.sourceRegisterNumber);
		String rightValue = NumberTools.numberToBinaryString(this.sourceLiteral, state.registerSize);
		
		if (this.token.equals("ADDI")) {
			BinaryOperationsResult val = BinaryOperations.add(leftSourceRegister.getValue(), rightValue);
			destinationRegister.setValue(val.result);
		} else if (this.token.equals("ADDIS")) {
			BinaryOperationsResult val = BinaryOperations.add(leftSourceRegister.getValue(), rightValue);
			destinationRegister.setValue(val.result);
			val.apply(state);
		} else if (this.token.equals("SUBI")) {
			BinaryOperationsResult val = null;
			if (this.constantOrderFlipped) {
				val = BinaryOperations.subtract(rightValue, leftSourceRegister.getValue());
			} else {
				val = BinaryOperations.subtract(leftSourceRegister.getValue(), rightValue);
			}
			destinationRegister.setValue(val.result);
		} else if (this.token.equals("SUBIS")) {
			BinaryOperationsResult val = null;
			if (this.constantOrderFlipped) {
				val = BinaryOperations.subtract(rightValue, leftSourceRegister.getValue());
			} else {
				val = BinaryOperations.subtract(leftSourceRegister.getValue(), rightValue);
			}
			destinationRegister.setValue(val.result);
			val.apply(state);
		} else if (this.token.equals("ANDI")) {
			//TODO
		} else if (this.token.equals("ORRI")) {
			//TODO
		} else if (this.token.equals("EORI")) {
			//TODO
		} else if (this.token.equals("LSL")) {
			//TODO
		} else if (this.token.equals("LSR")) {
			//TODO
		} else {
			System.out.println(this.token + " is unimplemented");
		}
	}

}
