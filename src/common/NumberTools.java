package common;
import java.util.ArrayList;
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
	
	public static ArrayList<String> splitAt(String input, Integer... index) {
		ArrayList<String> strs = new ArrayList<>();
		
		Integer lastCut = 0;
		for(Integer cut: index) {
			strs.add(input.substring(lastCut, cut));
			lastCut = cut;
		}
		
		strs.add(input.substring(lastCut));
		
		return strs;
	}

	//TODO: Do I need this?
	public static Object numberToBinaryString(Byte byte1, int width) {
		String binary = Integer.toBinaryString(byte1);
		String formatted = String.format("%" + width + "s", binary).replace(' ', byte1 < 0 ? '1' : '0');
		return formatted.substring(formatted.length() - width, formatted.length());
	}
}
