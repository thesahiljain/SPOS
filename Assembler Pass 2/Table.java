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
	
	public Table(String path) throws IOException {
		for(String line : new String(Files.readAllBytes(Paths.get(path))).split("\n")) {
			String tokens[] = line.trim().split("(\t| |,)+");
			RECORDS.add(new Record(tokens[0], tokens[1], tokens[2]));
		}
	}
	
	public String get(String index) {
		for(Record record : RECORDS) {
			if(record.index.equalsIgnoreCase(index))
				return record.value;
		}
		return "";
	}
	
	public void display() {
		for(Record record : RECORDS)
			System.out.println(record.index + "\t" + record.name + "\t" + record.value);
		System.out.println();
	}
}
