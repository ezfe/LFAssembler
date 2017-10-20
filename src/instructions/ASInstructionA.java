package instructions;
import java.util.Scanner;

import common.BinaryOperations;
import common.BinaryOperationsResult;
import common.Constants;
import common.IllegalRegisterException;
import common.NumberTools;
import simulator.SimulatorRegister;
import simulator.SimulatorState;

/**
 * 
 * @author Ezekiel Elin
 *
 */

public class ASInstructionA extends PerformableInstruction {
	
	/**
	 * Register
	 */
	private int destinationRegisterNumber = 0;
	
	/**
	 * Register
	 */
	private int leftSourceRegisterNumber = 0;
	
	/**
	 * Register
	 */
	private int rightSourceRegisterNumber = 0;
	
	public ASInstructionA(String token, Scanner scanner) throws IllegalRegisterException {
		this.token = token;
		
		String r1String = scanner.next();
		String r2String = scanner.next();
		String r3String = scanner.next();
		
		this.destinationRegisterNumber = (int) NumberTools.parseNumber(r1String.substring(1));
		this.leftSourceRegisterNumber = (int) NumberTools.parseNumber(r2String.substring(1));
		this.rightSourceRegisterNumber = (int) NumberTools.parseNumber(r3String.substring(1));
		
		AssemblerInstruction.checkRegister(destinationRegisterNumber);
		AssemblerInstruction.checkRegister(leftSourceRegisterNumber);
		AssemblerInstruction.checkRegister(rightSourceRegisterNumber);
	}
	
	public ASInstructionA(String token, String binaryRepresentation) {
		this.token = token;
		
		int start = Constants.OPCODE_LENGTH;
		int end = start + Constants.REGISTER_LENGTH;
		this.destinationRegisterNumber = NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));
		
		start = end;
		end += Constants.REGISTER_LENGTH;
		this.leftSourceRegisterNumber = NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));

		start = end;
		end += Constants.REGISTER_LENGTH;
		this.rightSourceRegisterNumber = NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));
	}
	
	public String sourceStringRepresentation() {
		return "" + this.token + " R" + this.destinationRegisterNumber + " R" + this.leftSourceRegisterNumber + " R"  + this.rightSourceRegisterNumber;
	}

	@Override
	public String binaryStringRepresentation() {
		String r1String = NumberTools.numberToBinaryString(this.destinationRegisterNumber, Constants.REGISTER_LENGTH);
		String r2String = NumberTools.numberToBinaryString(this.leftSourceRegisterNumber, Constants.REGISTER_LENGTH);
		String r3String = NumberTools.numberToBinaryString(this.rightSourceRegisterNumber, Constants.REGISTER_LENGTH);
		
		String instruction = this.opcodeBinaryString() + r1String + r2String + r3String;
		return NumberTools.rpad(instruction, '0', Constants.INSTRUCTION_LENGTH);
	}
	
	@Override
	public void perform(SimulatorState state) {
		super.perform(state);
		SimulatorRegister destinationRegister = state.getRegister(this.destinationRegisterNumber);
		SimulatorRegister leftSourceRegister = state.getRegister(this.leftSourceRegisterNumber);
		SimulatorRegister rightSourceRegister = state.getRegister(this.rightSourceRegisterNumber);
		
		if (this.token.equals("ADD")) {
			BinaryOperationsResult val = BinaryOperations.add(leftSourceRegister.getValue(), rightSourceRegister.getValue());
			destinationRegister.setValue(val.result);
		} else if (this.token.equals("ADDS")) {
			BinaryOperationsResult val = BinaryOperations.add(leftSourceRegister.getValue(), rightSourceRegister.getValue());
			destinationRegister.setValue(val.result);
			val.apply(state);
		} else if (this.token.equals("SUB")) {
			BinaryOperationsResult val = BinaryOperations.subtract(leftSourceRegister.getValue(), rightSourceRegister.getValue());
			destinationRegister.setValue(val.result);
		} else if (this.token.equals("SUBS")) {
			BinaryOperationsResult val = BinaryOperations.subtract(leftSourceRegister.getValue(), rightSourceRegister.getValue());
			destinationRegister.setValue(val.result);
			val.apply(state);
		} else if (this.token.equals("AND")) {
			BinaryOperationsResult val = BinaryOperations.and(leftSourceRegister.getValue(), rightSourceRegister.getValue());
			destinationRegister.setValue(val.result);
		} else if (this.token.equals("ORR")) {
			BinaryOperationsResult val = BinaryOperations.or(leftSourceRegister.getValue(), rightSourceRegister.getValue());
			destinationRegister.setValue(val.result);
		} else if (this.token.equals("EOR")) {
			BinaryOperationsResult val = BinaryOperations.xor(leftSourceRegister.getValue(), rightSourceRegister.getValue());
			destinationRegister.setValue(val.result);
		} else {
			System.out.println(this.token + " is unimplemented");
		}
	}
}
