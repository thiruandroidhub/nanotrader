package com.nanotrader.messaging;

import io.aeron.Aeron;
import io.aeron.Publication;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class AeronPublisher implements Runnable {
    private final Publication publication;
    private final int messageCount;

    public AeronPublisher(Aeron aeron, String channel, int streamId, int messageCount) {
        this.publication = aeron.addPublication(channel, streamId);
        this.messageCount = messageCount;
    }

    @Override
    public void run() {
        UnsafeBuffer buffer = new UnsafeBuffer(ByteBuffer.allocateDirect(256));

        for (int i = 0; i < messageCount; i++) {
            long timestamp = System.nanoTime();
            buffer.putLong(0, timestamp);

            while (publication.offer(buffer, 0, Long.BYTES) < 0) {
                Thread.yield(); // Back pressure
            }
        }

        System.out.println("Publisher done sending " + messageCount + " messages");
    }
}
