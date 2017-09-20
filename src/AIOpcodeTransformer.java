import java.util.Optional;

/**
 * 
 * @author Ezekiel Elin
 *
 */

public class AIOpcodeTransformer {
	public static Optional<Integer> getOpcode(String keyword){
		switch (keyword.toLowerCase()) {
		case "add":
			return Optional.of(1);
		case "subi":
			return Optional.of(2);
		}
		
		return Optional.empty();
	}
}
