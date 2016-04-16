package data_preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
		//	System.out.println(str[i]);
			if (data.containsKey(str[i])) {
				data.get(str[i]).add(no);
			} else {
				ArrayList<Integer> al = new ArrayList<Integer>();
				al.add(no);
				data.put(str[i], al);
			}
		}
	}
	
	public Preprocessing(Map<String, List<Integer>> da,int cou){
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
				deal_record(line,count);
				line = br.readLine();
				count++;
			}
			br.close();
			fr.close();
			System.out.println("Data Processing Finished!");
		} catch (FileNotFoundException e) {
			System.err.print("File can't open");
		} catch (IOException e) {
			System.err.print("File can't r/w");
		}
	}

}
