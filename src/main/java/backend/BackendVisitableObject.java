package backend;

public interface BackendVisitableObject<T> {
    T accept(BackendVisitor v);
}
