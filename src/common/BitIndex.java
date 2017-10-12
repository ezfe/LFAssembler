package common;
/**
 * 
 * @author Ezekiel Elin
 *
 */
public class BitIndex {
	private int bite = 0;
	private int bit = 0;
	
	public BitIndex(int bitIndex) {
		//TODO lol don't do this
		this.bit = bitIndex;
		while (this.bit >= 8) {
			bite++;
			bit -= 8;
		}
	}
	
	public int getByte() {
		return bite;
	}
	
	public void setByte(int bite) {
		this.bite = Math.max(0, bite);
	}
	
	public int getBit() {
		return bit;
	}
	
	public void setBit(int bit) {
		this.bit = Math.max(0, bit);
		while (this.bit >= 8) {
			bite++;
			bit -= 8;
		}
	}
}
