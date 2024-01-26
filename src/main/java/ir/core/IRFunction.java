package ir.core;

import java.util.ArrayList;
import java.util.List;

public class IRFunction implements IRVisitableObject {

    private final String name; /* !< Function name */
    private final List<IRValue> args; /* !< Function arguments */
    private final List<IRBlock> blocks; /* !< Basic blocks belonging to the function */
    private final IRType returnType;

    public IRFunction(String name, IRType returnType, List<IRType> argumentTypes) {
        this.name = name;
        args = new ArrayList<>();
        for (IRType type : argumentTypes) {
            args.add(new IRValue(type, null));
        }
        blocks = new ArrayList<IRBlock>();
        this.returnType = returnType;
    }

    public IRBlock addBlock() {
        IRBlock newBlock = new IRBlock(this);
    	blocks.add(newBlock);
    	return newBlock;
    }

    public String getName() {
        return name;
    }

    public List<IRBlock> getBlocks() {
        return blocks;
    }

    public List<IRValue> getArgs() {
        return args;
    }
    
    public IRType getReturnType() {
    	return returnType;
    }

    @Override
    public Object accept(IRVisitor v) {
        return v.visitFunction(this);
    }
}
