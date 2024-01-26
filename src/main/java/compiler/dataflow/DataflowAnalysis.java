package compiler.dataflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ir.core.IRBlock;
import ir.core.IRFunction;

public abstract class DataflowAnalysis <T> {

	protected boolean VERBOSE = true;
	
	HashMap<IRBlock, Set<T>> ins;
	HashMap<IRBlock, Set<T>> outs;
	List<IRBlock> worklist;
	
	abstract Set<T> gen(IRBlock b);
	abstract Set<T> kill(IRBlock b);
	abstract void propagate(IRBlock b);
	
	public DataflowAnalysis(IRFunction f) {
		ins = new HashMap<IRBlock, Set<T>>();
		outs = new HashMap<IRBlock, Set<T>>();
		worklist = new ArrayList<IRBlock>();
		
		for (IRBlock b : f.getBlocks()) {
			ins.put(b, new HashSet<T>());
			outs.put(b, new HashSet<T>());
		}
		
		worklist.addAll(f.getBlocks());
	}
	
	public void runAnalysis() {
		int step = 0;
		while (!worklist.isEmpty()) {
			IRBlock b = worklist.removeFirst();
			propagate(b);
		}
		
		if (VERBOSE) {
			System.out.println("****************************");
			System.out.println("Step " + step);
			for (IRBlock b : ins.keySet()) {
				System.out.println(" block_" + b.getBlockIndexInContainingFunc() + "\t ins:" + ins.get(b));
				System.out.println("       \t\t outs:" + outs.get(b));
			}
		}
	}
	
	
}
