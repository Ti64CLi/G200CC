package backend;

import java.util.ArrayList;
import java.util.List;

import ir.core.IRBlock;

public class BackendFunction implements BackendVisitableObject, LabelledBlock {
    private final String name;
    private final List<BackendBlock> blocks;

    public BackendFunction(String name) {
        this.name = name;
        this.blocks = new ArrayList<>();
    }

    public void addBlock(BackendBlock block) {
        blocks.add(block);
    }

    public String getName() {
        return name;
    }

    public List<BackendBlock> getBlocks() {
        return blocks;
    }

    @Override
    public Object accept(BackendVisitor v) {
        return v.visitFunction(this);
    }
    
    public String toString() {
    	String result = ".globl " + name + "\n" + name + ":\n";
    	for (BackendBlock b : blocks) {
    		result += b.toString() + "\n";
    	}
    	return result;
    }
    
    public String getLabel() {
    	return this.getName();
    }
}
