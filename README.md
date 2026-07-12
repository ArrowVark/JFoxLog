# Welcome to JFoxLog!

## What is JFoxLog?

JFoxLog is a Java library that brings [Foxglove]() logging support to FRC robots. Instead of having to manually control
data serialization and communication with [Foxglove](), JFoxLog abstracts this and gives programmers easy to use tools
while still allowing for complex control if needed. Additionally, JFoxLog runs alongside your existing robot code,
completely eliminating the need for an external processor on your robot. Because of its nature, JFoxLog also works in robot
simulations, though the ip address which [Foxglove]() connects to will be slightly different.

JFoxLog is built to work in tandem with [AdvantageKit](), with much of the JFoxLog syntax matching what you would expect
of [AdvantageKit](). In fact, JFoxLog can simply be added on top of an already existing [AdvantageKit]() project and
both will work with no downsides.

Since [Foxglove]()'s logging strategy is quite different than that of standard FRC logging tools, JFoxLog does not copy over
topics from network tables, but instead requires the programmer to define what values should be logged. In practice this has
been significantly more productive than simply mirroring network tables, and brings many features that work wonderfully for
[Foxglove]() which would have been a headache for other tools. One such feature I feel it is important to bring light to is
the ability (and heavy suggestion) to group logged values from one subsystem into a single topic, giving each subsystem its
own topic.

## Performance

JFoxLog is tested both in simulation and on actual competition robots to ensure JFoxLog doesn't severally impact robot
performance. Due to the nature of the performance testing, included is the performance metrics for all releases.
These metrics may not be up-to-date with the current release, but will likely be updated soon. If you would like to
add to the performance data, you can open an issue stating what you tested, hardware details, your robot's code
(if applicable), and what the loop times were.

> <details>
> <summary>Performance (v0.0.1)</summary>
>
> | Mode       | Average Base Loop Time | Average JFoxLog Loop Time | Average JFoxLog Overhead | Average JFoxLog Deviation | Average AdvantageKit Loop Time | Average AdvantageKit Overhead | Average AdvantageKit Deviation | Average Combined Loop Time | Average Combined Overhead | Average Combined Deviation |
> |------------|------------------------|---------------------------|--------------------------|---------------------------|--------------------------------|-------------------------------|--------------------------------|----------------------------|---------------------------|----------------------------|
> | Simulation | 00.00 ms               | 00.00 ms                  | 00.00 ms                 | 00.00 %                   | 00.00 ms                       | 00.00 ms                      | 00.00 %                        | 00.00 ms                   | 00.00 ms                  | 00.00 %                    |
> | Real       | 00.00 ms               | 00.00 ms                  | 00.00 ms                 | 00.00 %                   | 00.00 ms                       | 00.00 ms                      | 00.00 %                        | 00.00 ms                   | 00.00 ms                  | 00.00 %                    |
> 
> | Scenario                                 | Enviroment                                                                     | Base Loop Time | JFoxLog Loop Time | JFoxLog Overhead | JFoxLog Deviation | AdvantageKit Loop Time | AdvantageKit Overhead | AdvantageKit Deviation | Combined Loop Time | Combined Overhead | Combined Deviation |
> |------------------------------------------|--------------------------------------------------------------------------------|----------------|-------------------|------------------|-------------------|------------------------|-----------------------|------------------------|--------------------|-------------------|--------------------|
> | Library Sample Simulation                | 12th Gen Intel(R) Core(TM) i5-12450H (12) @ 4.40 Ghz, 18 GB Memory, Arch Linux | 00.00 ms       | 00.00 ms          | 00.00 ms         | 00.00 %           | 00.00 ms               | 00.00 ms              | 00.00 %                | 00.00 ms           | 00.00 ms          | 00.00 %            |
> | Library Sample Simulation                | 12th Gen Intel(R) Core(TM) i5-12450H (12) @ 4.40 Ghz, 18 GB Memory, Windows 11 |                |                   |                  |                   |                        |                       |                        |                    |                   |                    |
> | Library Sample On Robot                  | NI roboRIO 2.0                                                                 |                |                   |                  |                   |                        |                       |                        |                    |                   |                    |
> | [Team 31's 2026 Robot Code]() Simulation | 12th Gen Intel(R) Core(TM) i5-12450H (12) @ 4.40 Ghz, 18 GB Memory, Arch Linux |                |                   |                  |                   |                        |                       |                        |                    |                   |                    |
> | [Team 31's 2026 Robot Code]() On Robot   | NI roboRIO 2.0                                                                 |                |                   |                  |                   |                        |                       |                        |                    |                   |                    |
>
>
> </details>