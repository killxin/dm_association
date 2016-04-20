package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import apriori_association.Apriori;
import data_preprocessing.Preprocessing;

public class DemoUnixUsage {
	public static void main(String[] args) {
		Map<String, List<Integer>> data = new TreeMap<String, List<Integer>>();
		for(int i=0;i<9;i++){
			String path = "dataset//UNIX_usage//USER"+i+"//sanitized_all.981115184025";
			Preprocessing dp = new Preprocessing(path,1);
			Apriori ap = new Apriori(dp, 30f, 0.8f);
			ap.frequent_subset();
		//	ap.print_frequent_subset();
			ap.generate_rule();
			for(String rule : ap.rules){
				if (data.containsKey(rule)) {
					data.get(rule).add(i);
				} else {
					ArrayList<Integer> al = new ArrayList<Integer>();
					al.add(i);
					data.put(rule, al);
				}
			}
		}
		Preprocessing dp = new Preprocessing(data,9);
		Apriori ap = new Apriori(dp, 7f, 0.5f);
		ap.frequent_subset();
	//	ap.print_frequent_subset();
		ap.generate_rule();
	}
}
