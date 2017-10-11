package assembler;

import common.Token;

public class DirectiveDataContainer implements Token {
	public enum Size {
		DOUBLE, SINGLE, HALF, BYTE
	}
	
	private Size size;
	private Integer value;
	
	public DirectiveDataContainer(Size size, Integer value) {
		this.size = size;
		this.value = value;
	}
	
	public Size getSize() {
		return this.size;
	}
	
	public Integer getValue() {
		return this.value;
	}
	
	public Integer getWidth() {
		switch (this.size) {
		case DOUBLE:
			return 64;
		case SINGLE:
			return 32;
		case HALF:
			return 16;
		case BYTE:
			return 8;
		}
		return 8;
	}
	
	@Override
	public String toString() {
		return "Data{" + this.value + " IN " + this.size + "}";
	}
}
