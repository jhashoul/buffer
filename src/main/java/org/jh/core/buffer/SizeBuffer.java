package org.jh.core.buffer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SizeBuffer<T> {

    private int size;
    protected List<T> list;
    protected BufferAction<List<T>> a;
    protected Consumer<Exception> onFailure;

    public SizeBuffer(int size, BufferAction<List<T>> a, Consumer<Exception> onFailure) {
        this.a = a;
        this.size = size;
        this.onFailure = onFailure;
        this.list = new ArrayList<>(size);
    }

    public synchronized void append(T t) {
        list.add(t);
        if (size != 0 && list.size() >= size) {
            run();
        }
    }

    public synchronized void run() {
        if (!list.isEmpty()) {
            List<T> tl = list;
            list = new ArrayList<>(size);
            try {
                a.apply(tl);
            } catch (Exception e) {
                onFailure.accept(e);
            }
        }
    }
}
