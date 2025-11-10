# Nano Trader

Nano Trader is a playground for experimenting with ultra low-latency trading flows. The focus is on using Aeron as the primary execution layer while layering in tuning techniques (kernel/network tweaks, GC tuning, lock-free data paths, thread pinning, etc.) to shave the final microseconds off request/response loops.

## Goals
- Prototype Aeron-based order routing with repeatable latency benchmarks.
- Compare Aeron against alternate transport and serialization stacks.
- Catalog tuning knobs (JVM/GC, kernel bypass, NIC offloads, CPU pinning) and track their measured impact.

## Current Status
Active exploratory work. Expect rapid iteration and occasional breaking changes while latency experiments are run and recorded.

## Experiment Scope
Extend the Aeron IPC demo that currently sends a raw producer timestamp so it can transmit structured trading messages. Each payload carries:

- Instrument name (`string`)
- Buy price (`double`)
- Sell price (`double`)
- Producer timestamp (`long`)

## Latency Test Matrix
The following configurations are being measured to understand the cost of serialization, transport, and CPU pinning:

1. `Case 0` – Aeron IPC + no SBE (raw `long` payload)
2. `Case 1` – Aeron IPC + SBE + no CPU pinning
3. `Case 2` – Aeron UDP + SBE + no CPU pinning
4. `Case 3` – Aeron IPC + SBE + CPU pinning
5. `Case 4` – Aeron UDP + SBE + CPU pinning

Sample results from Case 0:

```
Publisher done sending 1000000 messages
Messages: 1,000,000 | Mean: 258.40 µs | 99th: 2463.89 µs | Max: 2473.14 µs
```

## Running the Benchmark
1. Open a terminal in the project directory.
2. Ensure the runner script is executable:
   ```
   chmod +x run.sh
   ```
3. Execute the benchmark:
   ```
   ./run.sh
   ```
4. Observe the latency summary printed at the end of the run (similar to the sample above).

