package org.jh.core;

import org.jh.core.buffer.Buffer;
import org.jh.core.buffer.BufferAction;
import org.jh.core.buffer.SizeBuffer;
import org.jh.core.buffer.TimeBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class BufferDemo {

    private static Logger logger = LoggerFactory.getLogger(BufferDemo.class);
    private static final Consumer<Exception> DEFAULT_ON_FAILURE = Exception::printStackTrace;
    private static final BufferAction<List<Integer>> ACTION = (l) -> logger.info("System time: {}, at this iteration there were {} entries in the list", System.currentTimeMillis(), l.size());

    public static void main(String[] args) {

        sizeBuffer();

        timeBuffer();

        buffer();

    }

    private static void sizeBuffer() {
        logger.info("-- SIZE BUFFER --");

        SizeBuffer buffer = new SizeBuffer<>(100,
                ACTION,
                DEFAULT_ON_FAILURE);

        IntStream.rangeClosed(1, 1000)
                .forEach(i -> buffer.append(i));

        buffer.shutdown();
        separator();
    }

    private static void timeBuffer() {
        logger.info("-- TIME BUFFER --");

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
        tb.shutdown();
        separator();
    }

    private static void buffer() {
        logger.info("-- BUFFER --");

        Buffer b = new Buffer<>(100,
                20,
                ACTION,
                DEFAULT_ON_FAILURE);

        IntStream.rangeClosed(1, 101)
                .forEach(i -> b.append(i));
        waitFor();
        b.shutdown();
        separator();
    }

    private static void separator() {
        logger.info("-----------------");
    }

    private static void waitFor() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
