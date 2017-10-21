package simulator;

import java.util.Collections;

import common.NumberTools;

public class SimulatorRegister {
	private String value;
	private int width;
	private int registerNumber;
	
	private boolean isLocked = false;
	
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
		return NumberTools.forcelpad(this.value, '0', this.width);
	}
	
	/**
	 * Get the register value as an integer
	 * @return The register value
	 */
	public long getNumericalValue() {
		return NumberTools.binaryStringToNumber(this.getValue());
	}
	
	/**
	 * Set the register value
	 */
	public void setValue(String newValue) {
		if (!this.isLocked) {
			this.value = NumberTools.forcelpad(newValue, '0', this.width);
		}
		// It's normal to try to modify locked registers, so there is
		// no negative feedback here
	}
	
	/**
	 * Lock the register (permanently)
	 */
	public void lockRegister() {
		this.isLocked = true;
	}
}
