package common;
import java.util.Scanner;

/**
 * 
 * @author Ezekiel Elin
 *
 */
public class Directive implements Token {
	private String token;
	private long value;
	
	public Directive(String token, Scanner scanner) {
		this.token = token;
		String valueString = scanner.next();
		
		Character numberType = 'd';
		if (valueString.length() > 1 && valueString.charAt(0) == '0') {
			numberType = valueString.toLowerCase().charAt(1);
		}
		switch (numberType) {
		case 'o':
			this.value = Long.parseLong(valueString.substring(2), 8);
			break;
		case 'x':
			this.value = Long.parseLong(valueString.substring(2), 16);
			break;
		case 'b':
			this.value = Long.parseLong(valueString.substring(2), 2);
			break;
		default:
			this.value = Long.parseLong(valueString, 10);
			break;
		}
	}
	
	public String getToken() {
		return this.token.toString();
	}
	
	public long getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return "Directive{" + this.token + ": " + this.value + "}";
	}
}
