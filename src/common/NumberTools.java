package common;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author Ezekiel Elin
 *
 */
public class NumberTools {
	/**
	 * Convert a long to a binary String.
	 * 
	 * @param number The number to convert into binary
	 * @param width The length of the output String
	 * @return The output String
	 */
	public static String numberToBinaryString(long number, int width) {
		String binary = Long.toBinaryString(number);
		String formatted = String.format("%" + width + "s", binary).replace(' ', number < 0 ? '1' : '0');
		return formatted.substring(formatted.length() - width, formatted.length());
	}
		
	/**
	 * Convert a long to a hex String
	 * 
	 * @param number The number to convert into binary
	 * @param width The length of the output String
	 * @return The output String
	 */
	public static String numberToHexString(long number, int width) {
		String hex = Long.toHexString(number);
		String formatted = String.format("%" + width + "s", hex).replace(' ', number < 0 ? 'F' : '0');
		return formatted.substring(formatted.length() - width, formatted.length());
	}
	
	public static int binaryStringToNumber(String string) {
		return Integer.parseInt(string, 2);
	}
	
	public static String rpad(String string, char fill, int width) {
		int needed = Math.max(width - string.length(), 0);
		
		char[] chars = new char[needed];
		Arrays.fill(chars, fill);
		String s = new String(chars);
		return string + s;
	}
	
	public static String lpad(String string, Character fill, int width) {
		int needed = Math.max(width - string.length(), 0);
		
		char[] chars = new char[needed];
		Arrays.fill(chars, fill);
		String s = new String(chars);
		return s + string;
	}
	
	public static ArrayList<String> splitAt(String input, int... index) {
		ArrayList<String> strs = new ArrayList<>();
		
		int lastCut = 0;
		for(int cut: index) {
			strs.add(input.substring(lastCut, cut));
			lastCut = cut;
		}
		
		strs.add(input.substring(lastCut));
		
		return strs;
	}
}
