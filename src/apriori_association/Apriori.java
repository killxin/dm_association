package apriori_association;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import data_preprocessing.Preprocessing;

public class Apriori {
	public List<Map<Set<String>, Integer>> CL = null;
	private Map<String, List<Integer>> data = null;
	private float min_sup = 0;
	private float min_conf = 0;
	
	public int num_fre = 0;

	public List<String> rules = null;
	
	public Apriori(Preprocessing dp, float mins, float minc) {
		CL = new ArrayList<Map<Set<String>, Integer>>();
		data = dp.data;
		min_sup = mins;
		min_conf = minc;
	}

	public void frequent_subset() {
		Map<Set<String>, Integer> L1 = new HashMap<Set<String>, Integer>();
		for (Map.Entry<String, List<Integer>> entry : data.entrySet()) {
			int val = entry.getValue().size();
			if (val >= min_sup) {
				Set<String> items = new TreeSet<String>();
				items.add(entry.getKey());
				L1.put(items, val);
			}
		}
		CL.add(L1);
		num_fre+=L1.size();
		for (int k = 1;; k++) {
			Map<Set<String>, Integer> C = supset_gen(k);
			Map<Set<String>, Integer> L = new HashMap<Set<String>, Integer>();
			for (Map.Entry<Set<String>, Integer> entry : C.entrySet()) {
				Set<String> strset = (Set<String>) entry.getKey();
				Set<Integer> result = null;
				for (String str : strset) {
					Set<Integer> ts = new TreeSet<Integer>();
					ts.addAll(data.get(str));
					if (result == null) {
						result = ts;
					} else {
						result.retainAll(ts);
					}
				}
				if (result.size() >= min_sup) {
					L.put(strset, result.size());
				}
			}
			if (L.isEmpty()) {
				// print_frequent_subset();
				break;
			}
			CL.add(L);
			num_fre+=L.size();
		}
	}

	public Map<Set<String>, Integer> supset_gen(int k) {
		Map<Set<String>, Integer> L = CL.get(k - 1);
		Map<Set<String>, Integer> C = new HashMap<Set<String>, Integer>();
		Set<Set<String>> sets = L.keySet();
		for (Set<String> set1 : sets) {
			for (Set<String> set2 : sets) {
				Set<String> newset = new TreeSet<String>();
				newset.addAll(set1);
				newset.addAll(set2);
				if (newset.size() == k + 1 && C.containsKey(newset) == false) {
					boolean sign = true;
					for (String str : newset) {
						Set<String> subset = new TreeSet<String>();
						subset.addAll(newset);
						subset.remove(str);
						if (L.containsKey(subset) == false) {
							sign = false;
							break;
						}
					}
					if (sign == true) {
						C.put(newset, 0);
					}
				}
			}
		}
		return C;
	}

	public void generate_rule() {
		rules = new ArrayList<String>();
		for (int i = 1; i < CL.size(); i++) {
			Map<Set<String>, Integer> l = CL.get(i);
			for (Set<String> sl : l.keySet()) {
				for (int j = 0; j < i; j++) {
					Map<Set<String>, Integer> s = CL.get(j);
					for (Set<String> ss : s.keySet()) {
						if (sl.containsAll(ss)) {
							float conf = (float) l.get(sl) / (float) s.get(ss);
							if (conf >= min_conf) {
								String rule = print_rule(sl, ss, conf);
								rules.add(rule);
							}
						}
					}
				}
			}
		}
	}

	private String print_rule(Set<String> sl, Set<String> ss, float conf) {
		String rule = new String();
		Set<String> l_s = new TreeSet<String>();
		l_s.addAll(sl);
		l_s.removeAll(ss);
		rule += "{";
		for (String str : ss) {
			rule += str+",";
		}
		rule += "} => {";
		for (String str : l_s) {
			rule += str+",";
		}
		rule += "}";
		System.out.println(rule+", conf = " + conf);
		return rule;
	}

	public void print_frequent_subset() {
		for (Map<Set<String>, Integer> mp : CL) {
			System.out.println("=====================");
			for (Set<String> set : mp.keySet()) {
				System.out.print("{");
				for (String str : set) {
					System.out.print(str + ",");
				}
				System.out.print("}");
				System.out.println("," + mp.get(set));
			}
			System.out.println("=====================");
		}
	}

	public static void main(String[] args) {
		Preprocessing datapre = new Preprocessing("src//demo//tst.csv");
		Apriori ap = new Apriori(datapre, 2, 0.3f);
		ap.frequent_subset();
		ap.print_frequent_subset();
		ap.generate_rule();
	}

}
