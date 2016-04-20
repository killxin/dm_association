package demo;

import baseline_association.Baseline;
import apriori_association.Apriori;
import data_preprocessing.Preprocessing;
import fpgrowth_association.FPgrowth;


public class DemoGrocerious {
	public static void main(String[] args) {
		Preprocessing dp = new Preprocessing("dataset//Groceries.csv");
		System.out.println("=========Apriori========");
		float mins = 0.01f * dp.count;
		Apriori ap = new Apriori(dp, mins, 0.5f);
		ap.frequent_subset();
	//	ap.print_frequent_subset();
		ap.generate_rule();
		System.out.println("=========FPGrowth========");
		FPgrowth fpg = new FPgrowth(dp, mins, 0.5f);
		fpg.fp_growth();
	//	fpg.print_frequent_subset();
		fpg.generate_rule();
		System.out.println("=========Baseline========");
		Baseline bl = new Baseline(dp, mins, 0.5f);
		bl.frequent_subset();
	//	bl.print_frequent_subset();
		bl.generate_rule();
	}
}
