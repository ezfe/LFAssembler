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
		this.bit = bitIndex;
		this.normalize();
	}
	
	public BitIndex(int byteIndex, int bitIndex) {
		this.bite = byteIndex;
		this.bit = bitIndex;
		this.normalize();
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
		this.normalize();
	}
	
	public void addBits(int bits) {
		this.bit += Math.max(0,  bits);
		this.normalize();
	}
	
	private void normalize() {
		while (this.bit >= 8) {
			bite++;
			bit -= 8;
		}
		while (this.bit < 0) {
			bite--;
			bit += 8;
		}
	}
	
	@Override
	public String toString() {
		return this.getByte() + "@" + this.getBit();
	}
	
	public BitIndex decrement() {
		return new BitIndex(this.getByte(), this.getBit() - 1);
	}
	
	public BitIndex increment() {
		return new BitIndex(this.getByte(), this.getBit() + 1);
	}
}
