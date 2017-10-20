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
public class ASInstructionB extends AssemblerInstruction implements Performable {

	/**
	 * Register
	 */
	private int r1 = 0;
	
	/**
	 * Register or numerical literal
	 */
	private int r2 = 0;
	
	/**
	 * Register or numerical literal
	 */
	private int r3 = 0;
	
	/**
	 * Indicates whether r2 is a numerical literal
	 */
	private boolean r2Constant = false;
	
	/**
	 * Indicates whether r3 is a numerical literal
	 */
	private boolean r3Constant = false;
	
	public ASInstructionB(String token, Scanner scanner) throws IllegalRegisterException {
		this.token = token;
		
		String r1String = scanner.next();
		String r2String = scanner.next();
		String r3String = scanner.next();
		
		r2Constant = (r2String.charAt(0) == '#');
		r3Constant = (r3String.charAt(0) == '#');
		
		if ((r2Constant && r3Constant) || !(r2Constant || r3Constant)) {
			System.err.println("Cannot have two or zero constants");
			assert false;
		}
		
		this.r1 = (int) NumberTools.parseNumber(r1String.substring(1));
		this.r2 = (int) NumberTools.parseNumber(r2String.substring(1));
		this.r3 = (int) NumberTools.parseNumber(r3String.substring(1));
		
		AssemblerInstruction.checkRegister(r1);
		if (!r2Constant) AssemblerInstruction.checkRegister(r2);
		if (!r3Constant) AssemblerInstruction.checkRegister(r3);
	}
	
	public ASInstructionB(String token, String binaryRepresentation) {
		this.token = token;
		
		int start = Constants.OPCODE_LENGTH;
		int end = start + Constants.REGISTER_LENGTH;
		this.r1 = NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));
		
		start = end;
		end += 1;
		this.r3Constant = binaryRepresentation.substring(start, end).equals("0") ? true : false;
		this.r2Constant = !r3Constant;
		
		start = end;
		end += Constants.REGISTER_LENGTH;
		this.r2 = NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));

		start = end;
		end += Constants.LITERAL_LENGTH;
		this.r3 = NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));
	}
	
	@Override
	public String sourceStringRepresentation() {
		return "" + this.token + " R" + this.r1 + " " + (this.r2Constant ? "#" : "R") + this.r2 + " "  + (this.r3Constant ? "#" : "R") + this.r3;
	}

	@Override
	public String binaryStringRepresentation() throws IllegalStateException {
		String r1String = NumberTools.numberToBinaryString(this.r1, Constants.REGISTER_LENGTH);
		String r2String = NumberTools.numberToBinaryString(this.r2, this.r2Constant ? Constants.LITERAL_LENGTH : Constants.REGISTER_LENGTH);
		String r3String = NumberTools.numberToBinaryString(this.r3, this.r3Constant ? Constants.LITERAL_LENGTH : Constants.REGISTER_LENGTH);
		
		String instruction = null;
		if (this.r2Constant) {
			instruction = this.opcodeBinaryString() + r1String + "1" + r3String + r2String;
		} else if (this.r3Constant) {
			instruction = this.opcodeBinaryString() + r1String + "0" + r2String + r3String;
		} else {
			throw new IllegalStateException("Cannot have two or zero constants");
		}
		
		return NumberTools.rpad(instruction, '0', Constants.INSTRUCTION_LENGTH);
	}

	@Override
	public void perform(SimulatorState state) {
		System.out.println("Performing " + this.sourceStringRepresentation());
		SimulatorRegister destinationRegister = state.getRegister(this.r1);
		SimulatorRegister leftSourceRegister = state.getRegister(this.r2);
		String rightValue = NumberTools.numberToBinaryString(this.r3, state.registerSize);
		
		if (this.token.equals("ADDI")) {
			BinaryOperationsResult val = BinaryOperations.add(leftSourceRegister.getValue(), rightValue);
			destinationRegister.setValue(val.result);
		} else if (this.token.equals("ADDIS")) {
			BinaryOperationsResult val = BinaryOperations.add(leftSourceRegister.getValue(), rightValue);
			destinationRegister.setValue(val.result);
			val.apply(state);
		} else if (this.token.equals("SUBI")) {
			BinaryOperationsResult val = null;
			if (this.r2Constant) {
				val = BinaryOperations.subtract(rightValue, leftSourceRegister.getValue());
			} else if (this.r3Constant) {
				val = BinaryOperations.subtract(leftSourceRegister.getValue(), rightValue);
			} else {
				throw new IllegalStateException("Cannot have neitehr r2 nor r3 constant");
			}
			destinationRegister.setValue(val.result);
		} else if (this.token.equals("SUBIS")) {
			BinaryOperationsResult val = null;
			if (this.r2Constant) {
				val = BinaryOperations.subtract(rightValue, leftSourceRegister.getValue());
			} else if (this.r3Constant) {
				val = BinaryOperations.subtract(leftSourceRegister.getValue(), rightValue);
			} else {
				throw new IllegalStateException("Cannot have neitehr r2 nor r3 constant");
			}
			destinationRegister.setValue(val.result);
			val.apply(state);
		} else {
			System.out.println(this.token + " is unimplemented");
		}
	}

}
