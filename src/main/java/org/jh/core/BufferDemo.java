package org.jh.core;

import org.jh.core.buffer.Buffer;
import org.jh.core.buffer.BufferAction;
import org.jh.core.buffer.SizeBuffer;
import org.jh.core.buffer.TimeBuffer;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class BufferDemo {

    private static final Consumer<Exception> DEFAULT_ON_FAILURE = Exception::printStackTrace;
    private static final BufferAction<List<Integer>> ACTION = (l) -> System.out.println(String.format("System time: [%d], at this iteration there were [%d] entries in the list", System.currentTimeMillis(), l.size()));

    public static void main(String[] args) {

        sizeBuffer();

        timeBuffer();

        buffer();

        System.exit(0);

    }

    private static void sizeBuffer() {
        SizeBuffer buffer = new SizeBuffer<>(100,
                ACTION,
                DEFAULT_ON_FAILURE);

        IntStream.rangeClosed(1, 1000)
                .forEach(i -> buffer.append(i));

        separator();
    }

    private static void timeBuffer() {
        TimeBuffer tb = new TimeBuffer<>(100,
                ACTION,
                DEFAULT_ON_FAILURE);

        IntStream.rangeClosed(1, 100)
                .forEach(i -> {
                    tb.append(i);
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        waitFor();
        separator();
    }

    private static void buffer() {
        Buffer tb = new Buffer<>(100,
                20,
                ACTION,
                DEFAULT_ON_FAILURE);

        IntStream.rangeClosed(1, 101)
                .forEach(i -> tb.append(i));
        waitFor();
        separator();
    }

    private static void separator() {
        System.out.println("\n---\n");
    }

    private static void waitFor() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
