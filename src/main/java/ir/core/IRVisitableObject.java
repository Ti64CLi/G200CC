package ir.core;

public interface IRVisitableObject<T> extends IRObject {

    public T accept(IRVisitor v);
}
