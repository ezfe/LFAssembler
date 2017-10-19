package simulator;

import java.util.Collections;

public class SimulatorRegister {
	private String value;
	private int width;
	private int registerNumber;
	
	/**
	 * Initialize the register with a given width between 1 and 64, inclusive
	 * @param registerSize
	 */
	public SimulatorRegister(int registerSize, int registerNumber) {
		this.registerNumber = registerNumber;
		if (registerSize > 0 && registerSize <= 64) {
			this.value = String.join("", Collections.nCopies(registerSize, "0"));
			this.width = registerSize;
		} else {
			throw new IllegalArgumentException("Register size cannot be " + registerSize);
		}
	}
	
	/**
	 * Get this register's number
	 * @return The register's number
	 */
	public int getRegisterNumber() {
		return this.registerNumber;
	}
	
	/**
	 * Get the register value
	 * @return The register value
	 */
	public String getValue() {
		return this.value.substring(this.value.length() - this.width, this.value.length());
	}
	
	/**
	 * Set the register value
	 */
	public void setValue(String newValue) {
		this.value = newValue.substring(newValue.length() - this.width, newValue.length());
	}
}
