import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PhaseTwo {
	public static void main(String[] args) throws IOException {
		// Load MNT, MDT and ALA
		MNT.read("mnt.txt");
		MDT.read("mdt.txt");
		ALA.read("ala.txt");
		
		MNT.display();
		System.out.println();
		MDT.display();
		System.out.println();
		ALA.display();
		System.out.println();
		
		// Other variables
		String outputCode = "";
		
		// Perform the algorithm
		for(String line : new String(Files.readAllBytes(Paths.get("input.asm"))).split("\n")) {
			line = line.trim();
			String tokens[] = line.split("( |,|\t)+");
			MNT.Record mnt = MNT.get(tokens[0]);
			if(mnt == null) {
				outputCode += line + "\n";
				if(line.equalsIgnoreCase("END"))
					break;
				continue;
			}
			int MDTP = mnt.mdtIndex;
			ALA.update(line, MDT.table.get(MDTP++).instruction);
			String expandedSource = "";
			while(true) {
				String mdtLine = MDT.table.get(MDTP++).instruction;
				mdtLine = ALA.substituteActual(mdtLine);
				if(mdtLine.equalsIgnoreCase("MEND")) {
					outputCode += expandedSource;
					break;
				}
				expandedSource += mdtLine + "\n";
			}
		}
		
		System.out.println("Final Code");
		System.out.println(outputCode);
	}
}

class MNT{
	static class Record{
		public int index;
		public String name;
		public int mdtIndex;
	}
	static public ArrayList<Record> table = new ArrayList<>();
	
	static public void read(String path) throws IOException {
		for(String line : new String(Files.readAllBytes(Paths.get(path))).split("\n")) {
			String tokens[] = line.trim().split("\t");
			Record record = new Record();
			record.index = Integer.parseInt(tokens[0]);
			record.name = tokens[1];
			record.mdtIndex = Integer.parseInt(tokens[2]);
			table.add(record);
		}
	}
	
	static public Record get(String token) {
		for(Record record : table) {
			if(token.equalsIgnoreCase(record.name))
				return record;
		}
		return null;
	}
	
	static public void display() {
		System.out.println("MNT - Macro Name Table");
		for(Record record : table)
			System.out.println(record.index + "\t" + record.name + "\t" + record.mdtIndex);
	}
}

class MDT{
	static class Record{
		public int index;
		public String instruction;
	}
	static public ArrayList<Record> table = new ArrayList<>();
	
	static public void read(String path) throws IOException {
		for(String line : new String(Files.readAllBytes(Paths.get(path))).split("\n")) {
			String tokens[] = line.trim().split("\t");
			Record record = new Record();
			record.index = Integer.parseInt(tokens[0]);
			record.instruction = tokens[1];
			table.add(record);
		}
	}
	
	static public void display() {
		System.out.println("MDT - Macro Definition Table");
		for(Record record : table)
			System.out.println(record.index + "\t" + record.instruction);
	}
}

class ALA{
	static class Record{
		public String index;
		public String name;
		public String actual;
	}
	static public ArrayList<Record> table = new ArrayList<>();
	
	static public void read(String path) throws IOException {
		for(String line : new String(Files.readAllBytes(Paths.get(path))).split("\n")) {
			String tokens[] = line.trim().split("\t");
			Record record = new Record();
			record.index = tokens[0];
			record.name = tokens[1];
			record.actual = tokens[2];
			table.add(record);
		}
	}
	
	static public void update(String line, String instruction) {
		String lineTokens[] = line.trim().split("( |,|\t)+");
		String instructionTokens[] = instruction.trim().split("( |,|\t)+");
		for(int i = 0; i < lineTokens.length; i++) {
			for(Record record : table) {
				if(record.name.equals(instructionTokens[i]))
					record.actual = lineTokens[i];
			}
		}
	}
	
	static public String substituteActual(String line) {
		for(String token : line.split("( |,|\t)+")) {
			for(Record record : table) {
				if(record.index.equals(token))
					line = line.replaceAll(token, record.actual);
			}
		}
		return line;
	}
	
	static public void display() {
		System.out.println("ALA - Argument List Array");
		for(Record record : table)
			System.out.println(record.index + "\t" + record.name + "\t" + record.actual);
	}
}