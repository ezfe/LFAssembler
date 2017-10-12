package assembler;

import common.Token;

public class DirectiveDataContainer implements Token {
	public enum Size {
		DOUBLE, SINGLE, HALF, BYTE
	}
	
	private Size size;
	private long value;
	
	public DirectiveDataContainer(Size size, long value) {
		this.size = size;
		
		boolean valueOK = true;
		switch (size) {
		case DOUBLE:
			valueOK = value >= Long.MIN_VALUE && value <= Long.MAX_VALUE;
			break;
		case SINGLE:
			valueOK = value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE;
			break;
		case HALF:
			valueOK = value >= Short.MIN_VALUE && value <= Short.MAX_VALUE;
			break;
		case BYTE:
			valueOK = value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE;
			break;
		}
		
		if (!valueOK) {
			System.err.println("Passed value " + value + " is greater than maximum allowed for " + size);
		}
		this.value = value;
	}
	
	public Size getSize() {
		return this.size;
	}
	
	public long getValue() {
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
