package com.nanotrader.messaging;

import io.aeron.Aeron;
import io.aeron.FragmentAssembler;
import io.aeron.Subscription;
import io.aeron.logbuffer.FragmentHandler;
import org.agrona.concurrent.Agent;
import org.agrona.concurrent.AgentRunner;
import org.agrona.concurrent.BusySpinIdleStrategy;

public class AeronSubscriber implements Runnable {
    private final Subscription subscription;
    private final LatencyStats latencyStats;

    public AeronSubscriber(Aeron aeron, String channel, int streamId, LatencyStats stats) {
        this.subscription = aeron.addSubscription(channel, streamId);
        this.latencyStats = stats;
    }

    @Override
    public void run() {
        FragmentHandler handler = new FragmentAssembler(
                (buffer, offset, length, header) -> {
                    long sentTime = buffer.getLong(offset);
                    long now = System.nanoTime();
                    latencyStats.record(now - sentTime);
                }
        );

        Agent agent = new Agent() {
            @Override
            public int doWork() {
                return subscription.poll(handler, 10);
            }

            @Override
            public String roleName() {
                return "subscriber-agent";
            }
        };

        AgentRunner runner = new AgentRunner(
                new BusySpinIdleStrategy(),
                Throwable::printStackTrace,
                null,
                agent
        );

        AgentRunner.startOnThread(runner);
    }
}
