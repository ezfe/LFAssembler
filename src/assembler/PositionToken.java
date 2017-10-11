package assembler;

import common.Token;

public class PositionToken implements Token {
	private Integer position;
	
	public PositionToken(Integer a) {
		this.position = a;
	}
	
	public Integer getPosition() {
		return this.position;
	}
	
	@Override
	public String toString() {
		return "Position{" + this.position + "}";
	}
}
