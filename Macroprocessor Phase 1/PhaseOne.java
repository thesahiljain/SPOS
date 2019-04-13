import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PhaseOne {
	
	public static void main(String[] args) throws IOException {
		
		// Output String
		String outputCode = "";
		// Declare counters
		int MNTC = 0;
		int MDTC = 0;
		// Declare flags
		boolean lookForMend = false;
		boolean firstLine = false;
		// Iterate over source code
		for(String line : new String(Files.readAllBytes(Paths.get("source.asm"))).split("\n")) {
			line = line.trim();
			String tokens[] = line.split("( |,|\t)+");
			if(tokens[0].equalsIgnoreCase("MACRO")) {
				lookForMend = true;
				firstLine = true;
				continue;
			}
			if(lookForMend) {
				if(firstLine) {
					MDT.add(MDTC++, line);
					MNT.add(MNTC, tokens[0], MDTC-1);
					for(int i = 1; i < tokens.length; i++)
						ALA.add(tokens[i]);
					firstLine = false;
					continue;
				}
				line = ALA.preprocess(line);
				MDT.add(MDTC++, line);
				if(tokens[0].equalsIgnoreCase("MEND")) lookForMend = false;
			}else
				outputCode += line + "\n";
		}
		//Display All
		System.out.println("Output Code");
		System.out.println(outputCode);
		System.out.println();
		MNT.display();
		System.out.println();
		MDT.display();
		System.out.println();
		ALA.display();
		System.out.println();
	}
}

class MNT{
	static class Record{
		public int index;
		public String name;
		public int mdtIndex;
	}
	static public ArrayList<Record> table = new ArrayList<>();
	static public void add(int index, String name, int mdtIndex) {
		Record record = new Record();
		record.index = index;
		record.name = name;
		record.mdtIndex = mdtIndex;
		table.add(record);
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
	static public void add(int index, String instruction) {
		Record record = new Record();
		record.index = index;
		record.instruction = instruction;
		table.add(record);
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
	static public void add(String name) {
		Record record = new Record();
		record.index = "#" + Integer.toString(table.size());
		record.name = name;
		record.actual = "dummy";
		table.add(record);
	}
	static public String preprocess(String line) {
		for(Record record : table)
			line = line.replaceAll(record.name, record.index);
		return line;
	}
	static public void display() {
		System.out.println("ALA - Argument List Array");
		for(Record record : table)
			System.out.println(record.index + "\t" + record.name + "\t" + record.actual);
	}
}