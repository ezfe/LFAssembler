package assembler;

import common.Token;

public class AlignToken implements Token {
	private Integer alignment;
	
	public AlignToken(Integer a) {
		this.alignment = a;
	}
	
	public Integer getAlignment() {
		return this.alignment;
	}
	
	@Override
	public String toString() {
		return "Align{" + this.alignment + "}";
	}
}
