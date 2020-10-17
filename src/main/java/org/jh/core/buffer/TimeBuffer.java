package org.jh.core.buffer;

import lombok.SneakyThrows;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class TimeBuffer<T> extends SizeBuffer<T> {

    private static final int TIME_BUFFER_SIZE = 0;

    private final long millis;
    private final AtomicBoolean active;
    private final ScheduledExecutorService executor;

    public TimeBuffer(long millis, BufferAction<List<T>> a, Consumer<Exception> onFailure) {
        this(millis, TIME_BUFFER_SIZE, a, onFailure);
    }

    protected TimeBuffer(long millis, int size, BufferAction<List<T>> a, Consumer<Exception> onFailure) {
        super(size, a, onFailure);
        this.millis = millis;
        this.active = new AtomicBoolean(false);
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public final synchronized void append(T t) {
        if (active.compareAndSet(false, true)) {
            executor.schedule(this::run, millis, TimeUnit.MILLISECONDS);
        }
        super.append(t);
    }

    @Override
    public final synchronized void run() {
        active.set(false);
        super.run();
    }

    @SneakyThrows
    @Override
    public synchronized void shutdown() {
        super.shutdown();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }
}
