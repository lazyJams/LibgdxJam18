package net.lazyio.astral.util.function;

@FunctionalInterface
public interface VoidTFunc<T, U, V> {

    void apply(T t, U u, V v);
}
