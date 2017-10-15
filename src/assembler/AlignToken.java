package assembler;

import common.Token;

public class AlignToken implements Token {
	private long alignment;
	
	public AlignToken(long l) {
		this.alignment = l;
	}
	
	public long getAlignment() {
		return this.alignment;
	}
	
	@Override
	public String toString() {
		return "Align{" + this.alignment + "}";
	}
}
