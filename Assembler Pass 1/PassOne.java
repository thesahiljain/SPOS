import java.io.*;
import java.nio.file.*;

public class PassOne {
	
	public static void main(String[] args) throws IOException {
		
		// Create and initialize MOT, POT and RT
		Table MOT = new Table("mot.txt");
		Table POT = new Table("pot.txt");
		Table RT = new Table("rt.txt");
		
		System.out.println("MOT Contents");
		MOT.display();
		System.out.println("POT Contents");
		POT.display();
		System.out.println("RT Contents");
		RT.display();
		
		// Create ST and LT
		Table ST = new Table();
		Table LT = new Table();
		
		// Other variables
		int locationCounter = 0;
		int poolTable[] = new int[10];
		int poolTablePointer = 0;
		
		// Read source code lines and perform operations to get IC
		for(String line : new String(Files.readAllBytes(Paths.get("source.asm"))).split("(\n)+")){
			String[] tokens = line.trim().split("( |\t|,)+");
			// Start and origin case, update location counter
			if(tokens[0].equalsIgnoreCase("START") || tokens[0].equalsIgnoreCase("ORIGIN")) {
				addIC(locationCounter, "AD", POT.get(tokens[0]), "", "C", tokens[1]);
				locationCounter = Integer.parseInt(tokens[1]);
				continue;
			}
			// EQU case, don't add to IC
			if(tokens.length >= 2)
			if(tokens[1].equalsIgnoreCase("EQU")) {
				ST.add(tokens[0], ST.get(tokens[2]));
				continue;
			}
			// LTORG and END case
			if(tokens[0].equalsIgnoreCase("LTORG") || tokens[0].equalsIgnoreCase("END")){
				for(int k = poolTable[poolTablePointer]; k < LT.currentIndex; k++) {
					LT.RECORDS.get(k).value = Integer.toString(locationCounter);
					addIC(locationCounter, "AD", POT.get(tokens[0]), "", "", LT.RECORDS.get(k).name);
					locationCounter++;
				}
				poolTablePointer++;
				poolTable[poolTablePointer] = LT.currentIndex;
				continue;
			}
			// Declaration statements case
			if(tokens[1].equalsIgnoreCase("DS") || tokens[1].equalsIgnoreCase("DC")) {
				// Add to ST
				if(ST.getRecord(tokens[0]) == null) {
					ST.add(tokens[0], Integer.toString(locationCounter));
				}else {
					ST.getRecord(tokens[0]).value = Integer.toString(locationCounter);
				}
				addIC(locationCounter, "DL", POT.get(tokens[1]), "", "C", tokens[2]);
				
				// Update Location counter
				int size = 1;
				if(tokens[1].equalsIgnoreCase("DS")) size = Integer.parseInt(tokens[2]);
				locationCounter += size;
				continue;
			}
			
			// Handling imperative statements, with or without labels
			
			// Check for label
			boolean hasLabel = false;
			if(MOT.getRecord(tokens[0]) == null && POT.getRecord(tokens[1]) == null)
				hasLabel = true;
			int offset = (hasLabel ? 1 : 0);
			
			// Add label and literal/symbol to LT or ST
			if(hasLabel)
				ST.add(tokens[0], Integer.toString(locationCounter));
			
			boolean poolFlag = true;
			// Avoid adding the same literal again if already exists in pool table
			for(int k = poolTable[poolTablePointer]; k < LT.currentIndex; k++) {
				if(tokens[2+offset].equalsIgnoreCase(LT.RECORDS.get(k).name)) {
					poolFlag = false;
				}
			}
			
			if(tokens[2+offset].startsWith("=")) {
				if(poolFlag)
					LT.add(tokens[2+offset], "");
				addIC(locationCounter, "IS", MOT.get(tokens[0+offset]), RT.get(tokens[1+offset]), "L", Integer.toString(LT.RECORDS.indexOf(LT.getRecord(tokens[2+offset]))));
			}else {
				if(ST.getRecord(tokens[2+offset]) == null)
					ST.add(tokens[2+offset], "");
				addIC(locationCounter, "IS", MOT.get(tokens[0+offset]), RT.get(tokens[1+offset]), "S", Integer.toString(ST.RECORDS.indexOf(ST.getRecord(tokens[2+offset]))));
			}
			locationCounter++;
			
		}
		
		// Display intermediate code
		System.out.println("Intermediate Code");
		System.out.println(intermediateCode);
		
		// Display ST and LT
		System.out.println("ST Contents");
		ST.display();
		System.out.println("LT Contents");
		LT.display();
	}
	
	// Declaration of IC and add method to add to it
	static String intermediateCode = "";
	static void addIC(int locationCounter, String directive, String opcode, String op1, String type, String op2) {
		if(!type.isEmpty())
			type = type + ", ";
		intermediateCode += locationCounter+"\t("+directive+", "+opcode+")\t"+op1+"\t"+type+op2+"\n";
	}
}
