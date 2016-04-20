package data_preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Preprocessing {
	public Map<String, List<Integer>> data = new TreeMap<String, List<Integer>>();
	public int count;

	private void deal_record(String line, int no) {
		String[] str = line.split(",");
		str[1] = str[1].substring(2);
		str[str.length - 1] = str[str.length - 1].substring(0,
				str[str.length - 1].length() - 2);

		for (int i = 1; i < str.length; i++) {
			// System.out.println(str[i]);
			if (data.containsKey(str[i])) {
				data.get(str[i]).add(no);
			} else {
				ArrayList<Integer> al = new ArrayList<Integer>();
				al.add(no);
				data.put(str[i], al);
			}
		}
	}

	public Preprocessing(Map<String, List<Integer>> da, int cou) {
		data = da;
		count = cou;
	}

	public Preprocessing(String path) {
		File ifile = new File(path);
		try {
			FileReader fr = new FileReader(ifile);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			line = br.readLine();
			count = 1;
			while (line != null) {
				deal_record(line, count);
				line = br.readLine();
				count++;
			}
			br.close();
			fr.close();
			System.out.println("Groceries Data Processing Finished!");
		} catch (FileNotFoundException e) {
			System.err.print("File can't open");
		} catch (IOException e) {
			System.err.print("File can't r/w");
		}
	}

	public Preprocessing(String path, int no) {
		File ifile = new File(path);
		try {
			FileReader fr = new FileReader(ifile);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			List<String> record = new ArrayList<String>();
			int count = 0;
			String current = null;
			while (line != null) {
				if (line.compareTo("**SOF**") == 0) {
					count++;
					record.clear();
					current = null;
				} else if (line.compareTo("**EOF**") == 0) {
					if(current != null && !record.contains(current)){
						record.add(current);
						//System.out.println(current);
					}
					for (String str : record) {
						//System.out.print(str+",");
						if (data.containsKey(str)) {
							data.get(str).add(count);
						} else {
							ArrayList<Integer> al = new ArrayList<Integer>();
							al.add(count);
							data.put(str, al);
						}
					}
				} else if(line.matches("^[a-z]*")){
					if(current != null && !record.contains(current)){
						record.add(current);
						//System.out.println(current);
					}
					current = line;
				} else if (line.matches("<[0-9]>")) {
					// current += " #";
				} else {
					// current += " "+line;
				}
				line = br.readLine();
			}
			br.close();
			fr.close();
			System.out.println("UNIX_usage Data Processing Finished!");
		} catch (FileNotFoundException e) {
			System.err.print("File can't open");
		} catch (IOException e) {
			System.err.print("File can't r/w");
		}
	}
	
	public static void main(String[] args){
		Preprocessing pre = new Preprocessing("dataset//UNIX_usage//USER0//sanitized_all.981115184025",1);
		for(Entry<String, List<Integer>> entry : pre.data.entrySet()){
			System.out.print(entry.getKey());
			System.out.print(",{");
			for(int i : entry.getValue()){
				System.out.print(i+",");
			}
			System.out.println("}");
		}
	}

}
