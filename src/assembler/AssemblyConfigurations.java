package assembler;

public class AssemblyConfigurations {
	private long wordSize = -1;
	private long registerCount = -1;
	private long maxMemory = -1;
	

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
	
	@Override
	public String toString() {
		return "Conf{wordSize: " + this.wordSize + ", registerCount: " + this.registerCount + ", maxMemory: " + this.maxMemory + "}";
	}
}
