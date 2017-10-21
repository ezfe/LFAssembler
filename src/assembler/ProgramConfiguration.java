package assembler;

import common.Constants;
import common.NumberTools;

public class ProgramConfiguration {
	private long wordSize = -1;
	private long registerCount = -1;
	private long maxMemory = -1;
	private long stackAddress = -1;
	
	public ProgramConfiguration() {
		
	}
	
	public ProgramConfiguration(String binaryRepresentation) {
		int start = 0;
		int end = start + 3;
		int wordSizeExponent = NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));
		
		start = end;
		end += 3;
		int registerCountExponent = NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));
		
		start = end;
		end += 6;
		int maxMemoryExponent = NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));
		
		start = end;
		end += 20;
		this.stackAddress = NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));
		
		this.wordSize = (long) Math.pow(2.0, (double) wordSizeExponent);
		this.registerCount = (long) Math.pow(2.0, (double) registerCountExponent);
		this.maxMemory = (long) Math.pow(2.0, (double) maxMemoryExponent);
	}
	
	public boolean wordSizeSet() {
		return this.wordSize >= 0;
	}
	public long getWordSize() {
		return wordSize;
	}
	public void setWordSize(long l) {
		this.wordSize = l;
	}
	public void unsetWordSize() {
		this.wordSize = -1;
	}
	
	public boolean registerCountSet() {
		return this.registerCount >= 0;
	}
	public long getRegisterCount() {
		return registerCount;
	}
	public void setRegisterCount(long registerCount) {
		this.registerCount = registerCount;
	}
	public void unsetRegisterCount() {
		this.registerCount = -1;
	}
	
	public boolean maxMemorySet() {
		return this.maxMemory >= 0;
	}
	public long getMaxMemory() {
		return maxMemory;
	}
	public void setMaxMemory(long maxMemory) {
		this.maxMemory = maxMemory;
	}
	public void unsetMaxMemory() {
		this.maxMemory = -1;
	}
	
	public boolean stackAddressSet() {
		return this.stackAddress >= 0;
	}
	public long getStackAddress() {
		return this.stackAddress;
	}
	public void setStackAddress(long stackAddress) {
		this.stackAddress = stackAddress;
	}
	public void unsetStackAddress() {
		this.stackAddress = -1;
	}
	
	@Override
	public String toString() {
		return "Conf{wordSize: " + this.wordSize + ", registerCount: " + this.registerCount + ", maxMemory: " + this.maxMemory + ", stackAddress: " + this.stackAddress + "}";
	}
	
	public String binaryStringRepresentation() {
		int wordsizeExponent = (int) (Math.log((double)this.getWordSize()) / Math.log(2));
		int registerCountExponent = (int) (Math.log((double) this.getRegisterCount()) / Math.log(2));
		int maxMemoryExponent = (int) (Math.log((double) this.getMaxMemory()) / Math.log(2));
		
		String wordsizeBinaryString = NumberTools.numberToBinaryString(wordsizeExponent, 3);
		String registerCountBinaryString = NumberTools.numberToBinaryString(registerCountExponent, 3);
		String maxMemoryBinaryString = NumberTools.numberToBinaryString(maxMemoryExponent, 6);
		String stackLocationBinaryString = NumberTools.numberToBinaryString((int) this.stackAddress, Constants.MEMADDR_LENGTH);
		
		String infoBytes = wordsizeBinaryString + registerCountBinaryString + maxMemoryBinaryString + stackLocationBinaryString;
		return infoBytes;
	}
}
