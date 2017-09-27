package common;
import java.util.Arrays;

/**
 * 
 * @author Ezekiel Elin
 *
 */
public class NumberTools {
	public static String numberToBinaryString(Integer number, Integer width) {
		String binary = Integer.toBinaryString(number);
		String formatted = String.format("%" + width + "s", binary).replace(' ', number < 0 ? '1' : '0');
		return formatted.substring(formatted.length() - width, formatted.length());
	}
	
	public static Integer binaryStringToNumber(String string) {
		return Integer.parseInt(string, 2);
	}
	
	public static String rpad(String string, Character fill, Integer width) {
		Integer needed = Math.max(width - string.length(), 0);
		
		char[] chars = new char[needed];
		Arrays.fill(chars, fill);
		String s = new String(chars);
		return string + s;
	}
	
	public static String lpad(String string, Character fill, Integer width) {
		Integer needed = Math.max(width - string.length(), 0);
		
		char[] chars = new char[needed];
		Arrays.fill(chars, fill);
		String s = new String(chars);
		return s + string;
	}
}
