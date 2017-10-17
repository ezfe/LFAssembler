package common;
import java.util.ArrayList;
import java.util.HashMap;
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
		this.value = NumberTools.parseNumber(scanner.next());
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
