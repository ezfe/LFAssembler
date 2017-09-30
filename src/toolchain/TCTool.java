package toolchain;

/**
 * The TCTool interface is used for all primary components of the toolchain
 * <p>
 * The run method is the entry point into the tool of the toolchain, and
 * receives all arguments passed into the original program.
 * 
 * @author Ezekiel Elin
 */

public interface TCTool {
	public String run(String[] args);
}
