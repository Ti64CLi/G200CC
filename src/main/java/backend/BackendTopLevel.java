package backend;

import java.util.ArrayList;
import java.util.List;

public class BackendTopLevel implements BackendVisitableObject {

    private final List<BackendFunction> functions;

    public BackendTopLevel() {
        functions = new ArrayList<>();
    }

    public void addFunction(BackendFunction function) {
        functions.add(function);
    }

    public List<BackendFunction> getFunctions() {
        return functions;
    }

    @Override
    public Object accept(BackendVisitor v) {
        return v.visitTopLevel(this);
    }
    
    public String toString() {
    	String result = "";
    	for (BackendFunction f : functions) {
    		if (!f.getBlocks().isEmpty()) //We dont print empty funcs
    			result += f.toString() + "\n"; 
    	}
    	return result;
    }
}
