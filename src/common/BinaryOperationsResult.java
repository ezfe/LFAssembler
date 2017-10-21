package common;

import simulator.SimulatorState;

public class BinaryOperationsResult {
		public String result;
		public boolean negative;
		public boolean zero;
		public boolean carry;
		public boolean overflow;
		public boolean halt;
		
		public BinaryOperationsResult(String result, boolean negative, boolean zero, boolean carry, boolean overflow, boolean halt) {
			this.result = result;
			this.negative = negative;
			this.zero = zero;
			this.carry = carry;
			this.overflow = overflow;
			this.halt = halt;
		}
		
		public BinaryOperationsResult(String result) {
			this(result, false, false, false, false, false);
		}
		
		public BinaryOperationsResult(boolean halt) {
			this("", false, false, false, false, halt);
		}
		
		public BinaryOperationsResult(String result, boolean negative, boolean zero, boolean carry, boolean overflow) {
			this(result, negative, zero, carry, overflow, false);
		}
		
		public void apply(SimulatorState state) {
			state.negativeFlag = this.negative;
			state.zeroFlag = this.zero;
			state.carryFlag = this.carry;
			state.overflowFlag = this.overflow;
		}
	}
	