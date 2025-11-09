package com.nanotrader;

import com.nanotrader.messaging.AeronPublisher;
import com.nanotrader.messaging.AeronSubscriber;
import com.nanotrader.messaging.LatencyStats;
import io.aeron.Aeron;
import io.aeron.driver.MediaDriver;

public class NanoTraderApp {
    public static void main(String[] args) throws InterruptedException {
        final String channel = "aeron:ipc";
        final int streamId = 10;
        final int messageCount = 1_000_000;

        MediaDriver driver = MediaDriver.launchEmbedded();
        Aeron aeron = Aeron.connect(new Aeron.Context().aeronDirectoryName(driver.aeronDirectoryName()));

        LatencyStats stats = new LatencyStats();

        Thread subscriberThread = new Thread(new AeronSubscriber(aeron, channel, streamId, stats));
        Thread publisherThread = new Thread(new AeronPublisher(aeron, channel, streamId, messageCount));

        subscriberThread.start();
        Thread.sleep(500); // Ensure subscriber is ready
        publisherThread.start();

        publisherThread.join();
        Thread.sleep(2000); // Wait for last messages
        stats.printSummary();

        aeron.close();
        driver.close();
    }
}
