package net.lazyio.astral.util.function;

@FunctionalInterface
public interface TFunc<T, U, V, R> {

    R apply(T t, U u, V v);
}
