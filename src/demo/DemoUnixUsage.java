package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import apriori_association.Apriori;
import data_preprocessing.Preprocessing;

public class DemoUnixUsage {

	public static void main(String[] args) {
		Map<String, List<Integer>> data = new TreeMap<String, List<Integer>>();
		for (int i = 0; i < 9; i++) {
			String path = "dataset//UNIX_usage//USER" + i
					+ "//sanitized_all.981115184025";
			Preprocessing dp = new Preprocessing(path, 1);
			Apriori ap = new Apriori(dp, 50f, 0.8f) {
				@Override
				public void generate_rule() {
					rules = new ArrayList<String>();
					for (int h = CL.size() - 1; h >= 0; h--) {
						Map<Set<String>, Integer> high = CL.get(h);
						for (Map.Entry<Set<String>, Integer> eh : high
								.entrySet()) {
							Set<String> sh = eh.getKey();
							if (eh.getValue() != -1) {
								for (int l = h - 1; l >= 0; l--) {
									Map<Set<String>, Integer> low = CL.get(l);
									for (Map.Entry<Set<String>, Integer> el : low
											.entrySet()) {
										Set<String> sl = el.getKey();
										if (el.getValue() != -1
												&& sh.containsAll(sl)) {
											low.put(sl, -1);
										}
									}
								}
								StringBuilder sb = new StringBuilder();
								for (String str : sh) {
									sb.append(str);
									sb.append(",");
								}
								rules.add(sb.toString());
							}
						}
					}
				}
			};
			ap.frequent_subset();
			// ap.print_frequent_subset();
			ap.generate_rule();
			//max_frequent_set
			for (String rule : ap.rules) {
				System.out.println(rule);
				if (data.containsKey(rule)) {
					data.get(rule).add(i);
				} else {
					ArrayList<Integer> al = new ArrayList<Integer>();
					al.add(i);
					data.put(rule, al);
				}
			}
		}
		System.out.println("===============");
		Preprocessing dp = new Preprocessing(data, 9);
		Apriori ap = new Apriori(dp, 2f, 0.5f);
		ap.frequent_subset();
		ap.print_frequent_subset();
	//	ap.generate_rule();
	}
}
