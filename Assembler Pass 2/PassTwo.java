import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PassTwo {
	public static void main(String[] args) throws IOException {
		// Create and initialize ST and LT
		Table ST = new Table("st.txt");
		Table LT = new Table("lt.txt");
		
		System.out.println("ST contents");
		ST.display();
		System.out.println("LT contents");
		LT.display();
		
		// Read IC code and generate machine code
		for(String line : new String(Files.readAllBytes(Paths.get("ic.txt"))).split("\n")) {
			String tokens[] = line.trim().replace('(', ' ').replace(')', ' ').split("(\t| |,)+");
			
			// Only IS
			if(tokens[1].equalsIgnoreCase("IS")) {
				String op1 = tokens[3];
				String op2 = "";
				if(tokens[4].equalsIgnoreCase("C"))
					op2 = tokens[5];
				if(tokens[4].equalsIgnoreCase("L"))
					op2 = LT.get(tokens[5]);
				if(tokens[4].equalsIgnoreCase("S"))
					op2 = ST.get(tokens[5]);
				addMC(tokens[0], tokens[2], op1, op2);
			}
		}
		
		// Display machine code
		System.out.println("Machine Code");
		System.out.println(machineCode);
	}
	
	static String machineCode = "";
	public static void addMC(String locationCounter, String code, String op1, String op2) {
		machineCode += locationCounter + "\t" + code + "\t" + op1 + "\t" + op2 + "\n";
	}
}
