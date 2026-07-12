package io.github.hudsoncrisp.jfoxlog;

import io.github.hudsoncrisp.jfoxlog.debug.FoxgloveDebugLogSeverity;
import io.github.hudsoncrisp.jfoxlog.debug.FoxgloveDebugPanel;
import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveWebSocketServer;

public class JFoxLog {
    static {
        FoxgloveDebugPanel.log("Base JFoxLog class loaded", FoxgloveDebugLogSeverity.PRERUN);
    }

    private static boolean periodicRan = false;

    private static boolean logLibraryComputeTime = false;
    private static boolean logPeriodicLoopTime = false;

    private static double libraryComputeTimeMillis = -1;
    private static long libraryComputeTimeNano = -1;
    private static long periodicLoopTimestampNano;
    private static double periodicLoopTimeMillis = -1;

    public static void logLibraryComputeTime(boolean value) {
        logLibraryComputeTime = value;
    }

    public static void logPeriodicLoopTime(boolean value) {
        logPeriodicLoopTime = value;
    }

    public static double getLibraryComputeTimeMillis() {
        return libraryComputeTimeMillis;
    }

    public static long getLibraryComputeTimeNano() {
        return libraryComputeTimeNano;
    }

    public static double getPeriodicLoopTimeMillis() {
        return periodicLoopTimeMillis;
    }

    public static void periodic() {
        if (!periodicRan) {
            periodicRan = true;
            FoxgloveDebugPanel.log("First periodic call", FoxgloveDebugLogSeverity.INFO);
        }
        timeDebugWrapper(FoxgloveWebSocketServer::periodic);
    }

    private static void timeDebugWrapper(Runnable runnable) {
        long startNano = 0;
        if (logPeriodicLoopTime || logLibraryComputeTime) startNano = System.nanoTime();
        if (logPeriodicLoopTime) {
            periodicLoopTimeMillis = (double) (startNano - periodicLoopTimestampNano) / 1000000;
            periodicLoopTimestampNano = startNano;
        }

        runnable.run();

        if (logPeriodicLoopTime) {
            long endNano = System.nanoTime();
            libraryComputeTimeMillis = (double) (endNano - startNano) / 1000000;
            libraryComputeTimeNano = endNano - startNano;
        }
    }
}
