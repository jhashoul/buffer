package org.jh.core.buffer;

import java.util.List;
import java.util.function.Consumer;

public class Buffer<T> extends TimeBuffer {

    public Buffer(long millis, int size, BufferAction<List<T>> a, Consumer<Exception> onFailure) {
        super(millis, size, a, onFailure);
    }

}
