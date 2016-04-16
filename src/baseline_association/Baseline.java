package baseline_association;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import apriori_association.Apriori;
import data_preprocessing.Preprocessing;

public class Baseline extends Apriori{
	public Baseline(Preprocessing dp, float mins, float minc) {
		super(dp, mins, minc);
		// TODO Auto-generated constructor stub
	}

	@Override
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
						C.put(newset, 0);
				}
			}
		}
		return C;
	}
	
	public static void main(String[] args) {
		Preprocessing datapre = new Preprocessing("src//demo//tst.csv");
		Baseline bl = new Baseline(datapre, 2, 0.3f);
		bl.frequent_subset();
		bl.print_frequent_subset();
		bl.generate_rule();
	}
}
