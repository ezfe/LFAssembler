package assembler;

public class AssemblyConfigurations {
	private Integer wordSize = -1;
	private Integer registerCount = -1;
	private Integer maxMemory = -1;
	

	public Boolean wordSizeSet() {
		return this.wordSize >= 0;
	}
	public Integer getWordSize() {
		return wordSize;
	}
	public void setWordSize(Integer wordSize) {
		this.wordSize = wordSize;
	}
	public void unsetWordSize() {
		this.wordSize = -1;
	}
	
	public Boolean registerCountSet() {
		return this.registerCount >= 0;
	}
	public Integer getRegisterCount() {
		return registerCount;
	}
	public void setRegisterCount(Integer registerCount) {
		this.registerCount = registerCount;
	}
	public void unsetRegisterCount() {
		this.registerCount = -1;
	}
	
	public Boolean maxMemorySet() {
		return this.maxMemory >= 0;
	}
	public Integer getMaxMemory() {
		return maxMemory;
	}
	public void setMaxMemory(Integer maxMemory) {
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
