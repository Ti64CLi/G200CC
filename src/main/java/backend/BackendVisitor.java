package backend;

public abstract class BackendVisitor<T> {
    public abstract T visitTopLevel(BackendTopLevel topLevel);

    public abstract T visitFunction(BackendFunction function);

    public abstract T visitBlock(BackendBlock block);

    public abstract T visitInstruction(BackendInstruction instruction);

    public abstract T visitInstructionOperand(BackendInstructionOperand operand);
}
