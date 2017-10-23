package common;

import java.util.Collections;
import java.util.HashMap;

import simulator.SimulatorController;

public class BinaryOperations {
	
	/**
	 * Add together two equally sized binary values
	 * @param left The first value
	 * @param right The second value
	 * @return The resulting value
	 */
	public static BinaryOperationsResult add(String left, String right) {
		if (left.length() != right.length()) {
			throw new IllegalArgumentException("Binary values must be the same width (" + left.length() + " =/= " + right.length() + ")");
		}
		
		HashMap<Integer, Boolean> carries = new HashMap<>();
		StringBuilder result = new StringBuilder(String.join("", Collections.nCopies(left.length(), "0")));
		
		boolean isZero = true;
		
		for(int i = left.length() - 1; i >= 0; i--) {
			char top = left.charAt(i);
			char bottom = right.charAt(i);
			boolean carry = carries.getOrDefault(new Integer(i), false);
			
			if (SimulatorController.verbose) System.out.println("Adding " + top + " + " + bottom + " (carry: " + carry + ")");
			
			int res = 0;
			
			if (!carry && top == '0' && bottom == '0') {
				res = 0;
			} else if (!carry && top == '0' && bottom == '1') {
				res = 1;
			} else if (!carry && top == '1' && bottom == '0') {
				res = 1;
			} else if (!carry  && top == '1' && bottom == '1') {
				res = 2;
			} else if (carry && top == '0' && bottom == '0') {
				res = 1;
			} else if (carry && top == '0' && bottom == '1') {
				res = 2;
			} else if (carry && top == '1' && bottom == '0') {
				res = 2;
			} else { // (carry && top == '1' && bottom == '1')
				res = 3;
			}
			
			if (res == 0 || res == 1) {
				result.setCharAt(i, res == 0 ? '0' : '1');
			} else if (res == 2 || res == 3) {
				result.setCharAt(i, res == 2 ? '0' : '1');
				carries.put(i - 1, true);
			}
			
			if (SimulatorController.verbose) System.out.println("Summed to " + res);
			if (SimulatorController.verbose) System.out.println("Stored as " + result.charAt(i));
			if (SimulatorController.verbose) System.out.println("Carried: " + carries.get(i - 1));

			if (result.charAt(i) != '0') {
				isZero = false;
			}
		}
		
		String resString = result.toString();
		BinaryOperationsResult res = new BinaryOperationsResult(resString);
		res.negative = resString.charAt(0) == '1' ? true : false;
		if (SimulatorController.verbose) System.out.println("Set negative flag to " + res.negative);
		res.zero = isZero;
		if (SimulatorController.verbose) System.out.println("Set zero flag to " + res.zero);
		res.carry = carries.getOrDefault(-1, false);
		if (SimulatorController.verbose) System.out.println("Set carry flag to " + res.carry);
		
		boolean i1positive = left.charAt(0) == '0';
		boolean i2positive = right.charAt(0) == '0';
		boolean r1positive = resString.charAt(0) == '0';
		res.overflow = (i1positive == i2positive) && (r1positive != i1positive);
		if (SimulatorController.verbose) System.out.println("Set overflow flag to " + res.overflow);
		
		return res;
	}
	
	/**
	 * Subtract two equally sized binary values
	 * @param left The first value
	 * @param right The second value
	 * @return The resulting value
	 */
	public static BinaryOperationsResult subtract(String left, String right) {
		if (left.length() != right.length()) {
			throw new IllegalArgumentException("Binary values must be the same width (" + left.length() + " =/= " + right.length() + ")");
		}
		
		if (SimulatorController.verbose) System.out.println("Flipping (1s-complement)");
		
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < right.length(); i++) {
			char c = right.charAt(i);
			if (c == '0') {
				result.append('1');
			} else { // c == '1'
				result.append('0');
			}
		}
		
		if (SimulatorController.verbose) System.out.println("Adding 1 (2s-complement)");
		String oneComp = result.toString();
		BinaryOperationsResult twoComp = BinaryOperations.add(oneComp, String.join("", Collections.nCopies(oneComp.length() - 1, "0")) + "1");
		
		if (SimulatorController.verbose) System.out.println("Adding together");
		BinaryOperationsResult res = BinaryOperations.add(left, twoComp.result);

		return res;
	}
	
	/**
	 * Logical AND two equally sized binary values
	 * @param left The first value
	 * @param right The second value
	 * @return The resulting value
	 */
	public static BinaryOperationsResult and(String left, String right) {
		if (left.length() != right.length()) {
			throw new IllegalArgumentException("Binary values must be the same width (" + left.length() + " =/= " + right.length() + ")");
		}
				
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < right.length(); i++) {
			if (left.charAt(i) == '0' || right.charAt(i) == '0') {
				if (SimulatorController.verbose) System.out.println("Writing 0 (not both 1)");
				result.append('0');
			} else { // left.charAt(i) == '1' && right.charAt(i) == '1'
				if (SimulatorController.verbose) System.out.println("Writing 1 (both 1)");
				result.append('1');
			}
		}
		
		BinaryOperationsResult res = new BinaryOperationsResult(result.toString());
		return res;
	}
	
	/**
	 * Logical OR two equally sized binary values
	 * @param left The first value
	 * @param right The second value
	 * @return The resulting value
	 */
	public static BinaryOperationsResult or(String left, String right) {
		if (left.length() != right.length()) {
			throw new IllegalArgumentException("Binary values must be the same width (" + left.length() + " =/= " + right.length() + ")");
		}
		
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < right.length(); i++) {
			if (left.charAt(i) == '1' || right.charAt(i) == '1') {
				if (SimulatorController.verbose) System.out.println("Writing 1 (either 1)");
				result.append('1');
			} else { // left.charAt(i) == '0' && right.charAt(i) == '0'
			if (SimulatorController.verbose) System.out.println("Writing 0 (both zero)");
				result.append('0');
			}
		}
		
		BinaryOperationsResult res = new BinaryOperationsResult(result.toString());
		return res;
	}
	
	/**
	 * Logical XOR two equally sized binary values
	 * @param left The first value
	 * @param right The second value
	 * @return The resulting value
	 */
	public static BinaryOperationsResult xor(String left, String right) {
		if (left.length() != right.length()) {
			throw new IllegalArgumentException("Binary values must be the same width (" + left.length() + " =/= " + right.length() + ")");
		}
		
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < right.length(); i++) {
			if ((left.charAt(i) == '1' && right.charAt(i) == '0') || (left.charAt(i) == '0' && right.charAt(i) == '1')) {
				if (SimulatorController.verbose) System.out.println("Writing 1 (either 10 or 01)");
				result.append('1');
			} else { // (left.charAt(i) == '0' && right.charAt(i) == '0') || (left.charAt(i) == '1' && right.charAt(i) == '1')
				if (SimulatorController.verbose) System.out.println("Writing 0 (either 00 or 11)");
				result.append('0');
			}
		}
		
		BinaryOperationsResult res = new BinaryOperationsResult(result.toString());
		return res;
	}
	
	/**
	 * Logical shift right
	 * @param value The value
	 * @param shift The shift amount
	 * @return The resulting value
	 */
	public static BinaryOperationsResult lsr(String value, int shift) {
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < value.length(); i++) {
			if (i < shift) {
				if (SimulatorController.verbose) System.out.println("Writing 0s for shift");
				result.append('0');
			} else {
				if (SimulatorController.verbose) System.out.println("Writing regular chars for shift");
				result.append(value.charAt(i - shift));
			}
		}
		
		BinaryOperationsResult res = new BinaryOperationsResult(result.toString());
		return res;
	}
	
	/**
	 * Logical shift left
	 * @param value The value
	 * @param shift The shift amount
	 * @return The resulting value
	 */
	public static BinaryOperationsResult lsl(String value, int shift) {
		StringBuilder result = new StringBuilder();
		if (SimulatorController.verbose) System.out.println("Writing regular chars for shift, using substring");
		result.append(value.substring(shift));
		while (result.length() < value.length()) {
			if (SimulatorController.verbose) System.out.println("Writing 0s for shift");
			result.append('0');
		}
		
		BinaryOperationsResult res = new BinaryOperationsResult(result.toString());
		return res;
	}
	
	/**
	 * Check whether a binary value is zero
	 * @param value The value
	 * @return Whether the value is zero
	 */
	public static boolean isZero(String value) {
		for(int i = 0; i < value.length(); i++) {
			if (value.charAt(i) != '0') {
				// Insecure
				if (SimulatorController.verbose) System.out.println("Found nonzero char, not zero");
				return false;
			}
		}
		return true;
	}
}
