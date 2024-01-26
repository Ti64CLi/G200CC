package ir.core;

import java.util.ArrayList;
import java.util.List;

public class IRTopLevel implements IRVisitableObject {

    private final List<IRFunction> functions; /* !< List of functions contained in the top level */

    public IRTopLevel() {
        this.functions = new ArrayList<IRFunction>();
        functions.add(new IRFunction("printChar", IRType.INT, List.of(IRType.INT)));
    }

    public void addFunction(IRFunction function) {
        functions.add(function);
    }

    public List<IRFunction> getFunctions() {
        return functions;
    }

    @Override
    public Object accept(IRVisitor v) {
        return v.visitTopLevel(this);
    }

}
