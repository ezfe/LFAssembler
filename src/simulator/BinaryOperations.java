package simulator;

import java.util.Collections;
import java.util.HashMap;

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
			} else if (carry && top == '1' && bottom == '1') {
				res = 3;
			}
			
			if (res == 0 || res == 1) {
				result.setCharAt(i, res == 0 ? '0' : '1');
			} else if (res == 2 || res == 3) {
				result.setCharAt(i, res == 2 ? '0' : '1');
				carries.put(i - 1, true);
			}

			if (result.charAt(i) != '0') {
				isZero = false;
			}
		}
		
		String resString = result.toString();
		BinaryOperationsResult res = new BinaryOperationsResult(resString);
		res.negative = resString.charAt(0) == '1' ? true : false;
		res.zero = isZero;
		res.carry = carries.getOrDefault(-1, false);
		
		boolean i1positive = left.charAt(0) == '0';
		boolean i2positive = right.charAt(0) == '0';
		boolean r1positive = resString.charAt(0) == '0';
		res.overflow = (i1positive == i2positive) && (r1positive != i1positive);
		
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
		
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < right.length(); i++) {
			char c = right.charAt(i);
			if (c == '0') {
				result.append('1');
			} else if (c == '1') {
				result.append('0');
			}
		}
		String oneComp = result.toString();
		BinaryOperationsResult twoComp = BinaryOperations.add(oneComp, String.join("", Collections.nCopies(oneComp.length() - 1, "0")) + "1");
		
		BinaryOperationsResult res = BinaryOperations.add(left, twoComp.result);

//		boolean i1positive = left.charAt(0) == '0';
//		boolean i2positive = right.charAt(0) == '0';
//		boolean r1positive = resString.charAt(0) == '0';
//		res.overflow = (i1positive != i2positive) && (r1positive != i1positive);
//		
		return res;
	}
}
