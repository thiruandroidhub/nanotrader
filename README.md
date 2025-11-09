# Nano Trader

This project exists to experiment with ultra low-latency trading flows. The focus is on using Aron as the primary execution layer and layering in other tuning techniques (kernel/network tweaks, GC tuning, lock-free data paths, etc.) to squeeze out the last microseconds from request/response loops.

## Goals
- Prototype Aron-based order routing with repeatable latency benchmarks.
- Compare Aron against alternate stack configurations.
- Catalogue tuning knobs (JVM/GC, kernel bypass, NIC offloads, thread pinning) and document their measured impact.

## Status
Active exploratory work. Expect rapid iteration and occasional breaking changes while latency experiments are run and recorded.
