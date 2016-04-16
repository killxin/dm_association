package fpgrowth_association;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import data_preprocessing.Preprocessing;

public class FPgrowth {
	private Map<Set<String>, Integer> CL = null;
	private float min_sup = 0;
	private float min_conf = 0;
	private FPTree whole = null;

	public FPgrowth(Preprocessing dp, float mins, float minc) {
		whole = new FPTree(dp, mins);
		min_sup = mins;
		min_conf = minc;
		CL = new HashMap<Set<String>, Integer>();
	//	whole.tree_print();
	}

	public FPTree fptree_gen(FPTree root, String str) {
		List<List<String>> src = new ArrayList<List<String>>();
		for (FPTableNode x : root.ftable) {
			if (x.id.compareTo(str) == 0) {
				FPTreeNode h = x.first;
				while (h != null) {
					List<String> record = new ArrayList<String>();
					FPTreeNode par = h.parent;
					while (par.item != null) {
						record.add(par.item);
						par = par.parent;
					}
					for (int i = 0; i < h.record; i++) {
						src.add(record);
					}
					h = h.next;
				}
				break;
			}
		}
		Map<String, List<Integer>> data = new TreeMap<String, List<Integer>>();
		int count = 1;
		for (List<String> strs : src) {
			for (int i = 0; i < strs.size(); i++) {
				// System.out.println(str[i]);
				if (data.containsKey(strs.get(i))) {
					data.get(strs.get(i)).add(count);
				} else {
					ArrayList<Integer> al = new ArrayList<Integer>();
					al.add(count);
					data.put(strs.get(i), al);
				}
			}
			count++;
		}
		Preprocessing dp = new Preprocessing(data, count);
		return new FPTree(dp, min_sup);
	}

	public void fp_growth() {
		List<String> pattern = new ArrayList<String>();
		fp_growth(whole, pattern);
	}

	private void fp_growth(FPTree root, List<String> pattern) {
		if (has_single_path(root)) {
		//	System.out.println("**over**");
			Map<Set<String>, Integer> path = new HashMap<Set<String>, Integer>();
			int count = 0;
			if (!root.ftree.child.isEmpty()){
				FPTreeNode h = root.ftree.child.get(0);
				while (h != null) {
					count++;
					Set<String> strs = new HashSet<String>();
				//	System.out.println("**" + h.item + h.record + "**");
					strs.add(h.item);
					path.put(strs, h.record);
					if (h.child.isEmpty())
						break;
					h = h.child.get(0);
				}
			}
			for (int i = 2; i <= count; i++) {
				frequent_subset(path, pattern);
				Map<Set<String>, Integer> new_path = new HashMap<Set<String>, Integer>();
				for (Map.Entry<Set<String>, Integer> e1 : path.entrySet()) {
					for (Map.Entry<Set<String>, Integer> e2 : path.entrySet()) {
						Set<String> ls = new HashSet<String>();
						ls.addAll(e1.getKey());
						ls.addAll(e2.getKey());
						if (ls.size() == i && !new_path.containsKey(ls)) {
							int sup = (e1.getValue() > e2.getValue()) ? e2
									.getValue() : e1.getValue();
							new_path.put(ls, sup);
						}
					}
				}
				path = new_path;
			}
		//	System.out.println("count:" + count);
			frequent_subset(path, pattern);

		} else {
			for (int i = root.ftable.size() - 1; i >= 0; i--) {
				FPTableNode x = root.ftable.get(i);
				pattern.add(x.id);
				FPTree new_root = fptree_gen(root, x.id);

		//		System.out.println("==" + x.id + "==");
		//		new_root.tree_print();

				Set<String> key = new HashSet<String>();
				key.addAll(pattern);
				CL.put(key, x.sup);
				
				if (new_root.ftree != null) {
					fp_growth(new_root, pattern);
				}

				pattern.remove(x.id);
			}
		}
	}

	private void frequent_subset(Map<Set<String>, Integer> path,
			List<String> pattern) {
		for (Map.Entry<Set<String>, Integer> e : path.entrySet()) {
			Set<String> key = new HashSet<String>();
			key.addAll(e.getKey());
			key.addAll(pattern);

			/*
			System.out.println("####");
			for (String s : key) {
				System.out.print(s + ",");
			}
			System.out.println("");
			*/
			CL.put(key, e.getValue());
		}
	}

	private boolean has_single_path(FPTree root) {
		FPTreeNode h = root.ftree;
		while (true) {
			List<FPTreeNode> chi = h.child;
			if (chi.isEmpty())
				return true;
			else if (chi.size() == 1) {
				h = chi.get(0);
			} else {
				return false;
			}
		}
	}

	public void generate_rule() {
		for(Map.Entry<Set<String>, Integer> e1 : CL.entrySet()){
			for(Map.Entry<Set<String>, Integer> e2 : CL.entrySet()){
				Set<String> s1 = e1.getKey();
				Set<String> s2 = e2.getKey();
				if(s1 != s2&&s1.containsAll(s2)){
					float conf = (float) e1.getValue() / (float) e2.getValue();
					if (conf >= min_conf) {
						print_rule(s1, s2, conf);
					}
				}
			}
		}
	}
	
	private void print_rule(Set<String> sl, Set<String> ss, float conf) {
		Set<String> l_s = new TreeSet<String>();
		l_s.addAll(sl);
		l_s.removeAll(ss);
		System.out.print("{");
		for (String str : ss) {
			System.out.print(str + ",");
		}
		System.out.print("} => {");
		for (String str : l_s) {
			System.out.print(str + ",");
		}
		System.out.print("}");
		System.out.println(", conf = " + conf);
	}
	
	public void print_frequent_subset() {
		System.out.println("==================");
		for (Map.Entry<Set<String>, Integer> e : CL.entrySet()) {
			System.out.print("{");
			for (String str : e.getKey()) {
				System.out.print(str + ",");
			}
			System.out.print("}");
			System.out.println("," + e.getValue());
		}
	}

	public static void main(String[] args) {
		Preprocessing dp = new Preprocessing("src//demo//tst.csv");
		FPgrowth fpg = new FPgrowth(dp, 2, 0.5f);
		fpg.fp_growth();
		fpg.print_frequent_subset();
		fpg.generate_rule();
	}
}
