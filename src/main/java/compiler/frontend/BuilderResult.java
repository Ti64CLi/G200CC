package compiler.frontend;

import ir.core.IRBlock;
import ir.core.IRValue;

public class BuilderResult {
	boolean hasBlock;
	IRBlock entry;
	IRBlock exit;
	IRValue value;

	public BuilderResult(boolean hasBlock, IRBlock entry, IRBlock exit, IRValue value){
		this.hasBlock = hasBlock;
		this.entry = entry;
		this.exit = exit;
		this.value = value;
	}
}
