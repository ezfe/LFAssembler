package common;

/**
 * 
 * @author Ezekiel Elin
 *
 */
public class Label implements Token {
	private String token;

	public Label(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return this.token;
	}
	
	@Override
	public String toString() {
		return "Label{" + this.token + "}";
	}
	
	@Override
	public int hashCode() {
		return this.token.hashCode();
	}
}
