package common;

import java.util.Collections;
import java.util.HashMap;

public class BinaryOperations {
	/**
	 * Add together two equally sized binary values
	 * @param left The first value
	 * @param right The second value
	 * @return The resulting value
	 */
	public static String add(String left, String right) {
		if (left.length() != right.length()) {
			throw new IllegalArgumentException("Binary values must be the same width (" + left.length() + " =/= " + right.length() + ")");
		}
		
		HashMap<Integer, Boolean> carries = new HashMap<>();
		StringBuilder result = new StringBuilder(String.join("", Collections.nCopies(left.length(), "0")));
		
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
			
			System.out.println(carry + " + " + top + " + " + bottom + " = " + result.charAt(i) + " carry " + carries.get(i));
		}
		
		return result.toString();
	}
}
