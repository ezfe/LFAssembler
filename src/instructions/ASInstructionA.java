package instructions;
import java.util.Scanner;

import common.BitSet;
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

public class ASInstructionA extends PerformableInstruction {
	
	/**
	 * Register
	 */
	private int r1 = 0;
	
	/**
	 * Register
	 */
	private int r2 = 0;
	
	/**
	 * Register
	 */
	private int r3 = 0;
	
	public ASInstructionA(String token, Scanner scanner) throws IllegalRegisterException {
		this.token = token;
		
		String r1String = scanner.next();
		String r2String = scanner.next();
		String r3String = scanner.next();
		
		this.r1 = (int) NumberTools.parseNumber(r1String.substring(1));
		this.r2 = (int) NumberTools.parseNumber(r2String.substring(1));
		this.r3 = (int) NumberTools.parseNumber(r3String.substring(1));
		
		AssemblerInstruction.checkRegister(r1);
		AssemblerInstruction.checkRegister(r2);
		AssemblerInstruction.checkRegister(r3);
	}
	
	public ASInstructionA(String token, String binaryRepresentation) {
		this.token = token;
		
		int start = Constants.OPCODE_LENGTH;
		int end = start + Constants.REGISTER_LENGTH;
		this.r1 = NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));
		
		start = end;
		end += Constants.REGISTER_LENGTH;
		this.r2 = NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));

		start = end;
		end += Constants.REGISTER_LENGTH;
		this.r3 = NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));
	}
	
	public String sourceStringRepresentation() {
		return "" + this.token + " R" + this.r1 + " R" + this.r2 + " R"  + this.r3;
	}

	@Override
	public String binaryStringRepresentation() {
		String r1String = NumberTools.numberToBinaryString(this.r1, Constants.REGISTER_LENGTH);
		String r2String = NumberTools.numberToBinaryString(this.r2, Constants.REGISTER_LENGTH);
		String r3String = NumberTools.numberToBinaryString(this.r3, Constants.REGISTER_LENGTH);
		
		String instruction = this.opcodeBinaryString() + r1String + r2String + r3String;
		return NumberTools.rpad(instruction, '0', Constants.INSTRUCTION_LENGTH);
	}
	
	@Override
	public void perform(SimulatorState state) {
		super.perform(state);
		SimulatorRegister destinationRegister = state.getRegister(this.r1);
		SimulatorRegister leftSourceRegister = state.getRegister(this.r2);
		SimulatorRegister rightSourceRegister = state.getRegister(this.r3);
		
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
			//TODO
		} else if (this.token.equals("ORR")) {
			//TODO
		} else if (this.token.equals("EOR")) {
			//TODO
		} else {
			System.out.println(this.token + " is unimplemented");
		}
	}
}
