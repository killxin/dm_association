package fpgrowth_association;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import data_preprocessing.Preprocessing;

class FPTreeNode {
	public String item;
	public int record;
	public List<FPTreeNode> child;
	public FPTreeNode parent;
	public FPTreeNode next;

	public FPTreeNode(String i) {
		item = i;
		record = 1;
		child = new ArrayList<FPTreeNode>();
		parent = null;
		next = null;
	}
}

class FPTableNode {
	public String id;
	public int sup;
	public FPTreeNode first;

	public FPTableNode(String id, int sup) {
		this.id = id;
		this.sup = sup;
		first = null;
	}
}

class FPcmp implements Comparator<FPTableNode> {
	@Override
	public int compare(FPTableNode arg0, FPTableNode arg1) {
		if(arg0.sup > arg1.sup)
			return -1;
		else if(arg0.sup == arg1.sup)
			return 0;
		return 1;
	}
}

public class FPTree {
	private Map<String, List<Integer>> data = null;
	private List<List<String>> raw = null;
	public List<FPTableNode> ftable = null;
	public FPTreeNode ftree = null;
	private float min_sup = 0;

	public FPTree(Preprocessing dp, float mins) {
		ftable = new ArrayList<FPTableNode>();
		ftree = new FPTreeNode(null);
		raw = new ArrayList<List<String>>();
		data = dp.data;
		min_sup = mins;
		
		for (Map.Entry<String, List<Integer>> entry : data.entrySet()) {
			int val = entry.getValue().size();
			if (val >= min_sup) {
				FPTableNode fpt = new FPTableNode(entry.getKey(), val);
				ftable.add(fpt);
			}
		}

		ftable.sort(new FPcmp());

		for (int i = 1; i < dp.count; i++) {
			List<String> al = new ArrayList<String>();
			for (FPTableNode x : ftable) {
				List<Integer> li = data.get(x.id);
				if (li.contains(i)) {
					// al.add(x.id+li.size());
					al.add(x.id);
				}
			}
			if (!al.isEmpty()) {
				raw.add(al);
			}
		}

		fptree_gen();
	}

	private void fptree_gen() {
		for (List<String> strs : raw) {
		//	System.out.println(strs);
			insert_tree(strs, 0, ftree);
		}
	}

	private void insert_tree(List<String> strs, int k, FPTreeNode par) {
		if (k >= strs.size())
			return;
		String str = strs.get(k);
		FPTreeNode cur_node = null;
		for (FPTreeNode chi : par.child) {
			if (chi.item.compareTo(str) == 0) {
				chi.record++;
				cur_node = chi;
				break;
			}
		}
		if (cur_node == null) {
			cur_node = new FPTreeNode(str);
			cur_node.parent = par;
			cur_node.next = null;
			par.child.add(cur_node);
			// link to fptable
			for (FPTableNode fpt : ftable) {
				if (fpt.id.compareTo(str) == 0) {
					cur_node.next = fpt.first;
					fpt.first = cur_node;
				}
			}
		}
		insert_tree(strs, k + 1, cur_node);
	}

	private void tree_print(FPTreeNode par, int space) {
		for (int i = 0; i < space; i++) {
			System.out.print(" ");
		}
		System.out.println(par.item + par.record);
		for (FPTreeNode x : par.child) {
			tree_print(x, space + 2);
		}
	}
	public void tree_print(){
		tree_print(ftree, 0);
	}
	
	public void test_print() {
		for (FPTableNode x : ftable) {
			System.out.println(x.id + " " + x.sup);
			FPTreeNode fpt = x.first;
			while (fpt != null) {
				System.out.println(fpt.item + ", ");
				fpt = fpt.next;
			}
			System.out.println("");
		}
		for (List<String> str : raw) {

			for (String x : str) {
				System.out.print(x + ", ");
			}
			System.out.println("");

		}
		System.out.println(raw.size());
		tree_print(ftree, 0);
	}

	public static void main(String[] args) {
		Preprocessing dp = new Preprocessing("src//demo//tst.csv");
		FPTree fp = new FPTree(dp, 2);
		fp.test_print();
	}
}
