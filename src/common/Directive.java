package common;
import java.util.Scanner;

/**
 * 
 * @author Ezekiel Elin
 *
 */
public class Directive implements Token {
	private String token;
	private Integer value;
	
	public Directive(String token, Scanner scanner) {
		this.token = token;
		String valueString = scanner.next();
		
		Character numberType = 'd';
		if (valueString.charAt(0) == '0') {
			numberType = valueString.charAt(1);
		}
		switch (numberType) {
		case 'o':
			this.value = Integer.parseInt(valueString.substring(2), 8);
			break;
		case 'x':
			this.value = Integer.parseInt(valueString.substring(2), 16);
			break;
		case 'b':
			this.value = Integer.parseInt(valueString.substring(2), 2);
			break;
		default:
			this.value = Integer.parseInt(valueString, 10);
			break;
		}
	}
	
	public String getToken() {
		return this.token.toString();
	}
	
	public String getValue() {
		return this.value.toString();
	}

	@Override
	public String toString() {
		return "Directive{" + this.token + ": " + this.value + "}";
	}
}
