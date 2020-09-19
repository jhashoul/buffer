package org.jh.core.buffer;

@FunctionalInterface
public interface BufferAction<T> {
    void apply(T t) throws Exception;
}
