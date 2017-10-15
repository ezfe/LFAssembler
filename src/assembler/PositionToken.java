package assembler;

import common.Token;

public class PositionToken implements Token {
	private long position;
	
	public PositionToken(long l) {
		this.position = l;
	}
	
	public long getPosition() {
		return this.position;
	}
	
	@Override
	public String toString() {
		return "Position{" + this.position + "}";
	}
}
