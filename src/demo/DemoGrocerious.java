package demo;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

import baseline_association.Baseline;
import apriori_association.Apriori;
import data_preprocessing.Preprocessing;
import fpgrowth_association.FPgrowth;


public class DemoGrocerious {
	public static void main(String[] args) {
		long startTime,endTime,startmm,endmm;
		Preprocessing dp = new Preprocessing("dataset//Groceries.csv");
		float mins = 0.01f * dp.count;
		MemoryMXBean mmb = ManagementFactory.getMemoryMXBean();
		System.out.println("=========Apriori========");
		startTime=System.currentTimeMillis();
		startmm = mmb.getHeapMemoryUsage().getUsed();
		Apriori ap = new Apriori(dp, mins, 0.5f);
		ap.frequent_subset();
		endTime=System.currentTimeMillis();
		endmm = mmb.getHeapMemoryUsage().getUsed();
		System.out.printf("runtime:%.2fs\n",(float)(endTime-startTime)/1000f);
		System.out.printf("heapusage:%.2fMB\n",(float)(endmm-startmm)/1024f/1024f);
		System.out.printf("frequentsubsets:%d\n",ap.num_fre);
	//	ap.print_frequent_subset();
		ap.generate_rule();
		
		System.out.println("=========FPGrowth========");
		startTime=System.currentTimeMillis();
		startmm = mmb.getHeapMemoryUsage().getUsed();
		FPgrowth fpg = new FPgrowth(dp, mins, 0.5f);
		fpg.fp_growth();
		endTime=System.currentTimeMillis();
		endmm = mmb.getHeapMemoryUsage().getUsed();
		System.out.printf("runtime:%.2fs\n",(float)(endTime-startTime)/1000f);
		System.out.printf("heapusage:%.2fMB\n",(float)(endmm-startmm)/1024f/1024f);
		System.out.printf("frequentsubsets:%d\n",fpg.num_fre);
	//	fpg.print_frequent_subset();
		fpg.generate_rule();
		
		System.out.println("=========Baseline========");
		startTime=System.currentTimeMillis();
		startmm = mmb.getHeapMemoryUsage().getUsed();
		Baseline bl = new Baseline(dp, mins, 0.5f);
		bl.frequent_subset();
		endTime=System.currentTimeMillis();
		endmm = mmb.getHeapMemoryUsage().getUsed();
		System.out.printf("runtime:%.2fs\n",(float)(endTime-startTime)/1000f);
		System.out.printf("heapusage:%.2fMB\n",(float)(endmm-startmm)/1024f/1024f);
		System.out.printf("frequentsubsets:%d\n",bl.num_fre);
	//	bl.print_frequent_subset();
		bl.generate_rule();
	}
}
