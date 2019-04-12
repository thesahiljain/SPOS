import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Table {
	class Record{
		public String index;
		public String name;
		public String value;
		public Record(String index, String name, String value) {
			this.index = index;
			this.name = name;
			this.value = value;
		}
	}
	public ArrayList<Record> RECORDS = new ArrayList<>();
	
	// Current index exclusively used for LT
	public int currentIndex;
	public Table() {currentIndex=0;}
	
	public Table(String path) throws IOException {
		for(String line : new String(Files.readAllBytes(Paths.get(path))).split("\n")) {
			String[] tokens = line.split(" ");
			RECORDS.add(new Record(tokens[0].trim(), tokens[1].trim(), tokens[2].trim()));
		}
	}
	
	public void display() {
		for(Record record : RECORDS)
			System.out.println(record.index + "\t" + record.name + "\t" + record.value);
		System.out.println();
	}
	
	public void add(String key, String value) {
		RECORDS.add(new Record(Integer.toString(currentIndex), key, value));
		currentIndex++;
	}
	
	public String get(String key) {
		String value = "";
		for(Record record : RECORDS) {
			if(record.name.equalsIgnoreCase(key)){
				value = record.value;
				break;
			}
		}
		return value;
	}
	
	// For modifying
	public Record getRecord(String key) {
		for(Record record : RECORDS) {
			if(record.name.equalsIgnoreCase(key)){
				return record;
			}
		}
		return null;
	}
}
