package io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket;

import io.github.hudsoncrisp.jfoxlog.debug.FoxgloveDebugLogMeta;
import io.github.hudsoncrisp.jfoxlog.debug.FoxgloveDebugPanel;

public class FoxgloveLoggingFrequencyInfo {
    public static final FoxgloveLoggingFrequencyInfo NO_LEAD = FoxgloveLoggingFrequencyInfo.fromMillisecondLead(0);
    public static final FoxgloveLoggingFrequencyInfo DEFAULT_FREQUENCY = FoxgloveLoggingFrequencyInfo.fromMillisecondLead(0);
    public static final FoxgloveLoggingFrequencyInfo DEFAULT_ROBOT_CODE_FREQUENCY = FoxgloveLoggingFrequencyInfo.fromMillisecondLead(0);
    public static final FoxgloveLoggingFrequencyInfo FULL_LOGGING = FoxgloveLoggingFrequencyInfo.fromMillisecondLead(0);

    public static final FoxgloveLoggingFrequencyInfo NO_AUTO_LOGGING = FoxgloveLoggingFrequencyInfo.fromMillisecondLead(-1);

    public static final FoxgloveLoggingFrequencyInfo DEFAULT_LOW_FREQUENCY = FoxgloveLoggingFrequencyInfo.fromHertzFrequency(0.5);

    public enum DataRetrieveType {
        NEW_DATA,
        CACHE
    }

    private double millisecondLead;
    private long lastExecutionNanos = -1;

    private DataRetrieveType dataRetrieveType;

    private FoxgloveLoggingFrequencyInfo(double millisecondLead, DataRetrieveType dataRetrieveType) {
        this.millisecondLead = millisecondLead;
        this.dataRetrieveType = dataRetrieveType;
    }

    public static FoxgloveLoggingFrequencyInfo fromMillisecondLead(double milliseconds) {
        return fromMillisecondLead(milliseconds, DataRetrieveType.NEW_DATA);
    }

    public static FoxgloveLoggingFrequencyInfo fromMillisecondLead(double milliseconds, DataRetrieveType dataRetrieveType) {
        return new FoxgloveLoggingFrequencyInfo(milliseconds, dataRetrieveType);
    }

    public static FoxgloveLoggingFrequencyInfo fromHertzFrequency(double hertz) {
        return fromHertzFrequency(hertz, DataRetrieveType.NEW_DATA);
    }

    public static FoxgloveLoggingFrequencyInfo fromHertzFrequency(double hertz, DataRetrieveType dataRetrieveType) {
        return new FoxgloveLoggingFrequencyInfo(1_000 / hertz, dataRetrieveType);
    }

    public FoxgloveLoggingFrequencyInfo setMillisecondLead(double milliseconds) {
        this.millisecondLead = milliseconds;
        return this;
    }

    public FoxgloveLoggingFrequencyInfo setHertzFrequency(double hertz) {
        this.millisecondLead = 1_000 / hertz;
        return this;
    }

    public FoxgloveLoggingFrequencyInfo setDataRetrieveType(DataRetrieveType dataRetrieveType) {
        this.dataRetrieveType = dataRetrieveType;
        return this;
    }

    public DataRetrieveType getDataRetrieveType() {
        return dataRetrieveType;
    }

    public boolean hasSurpassedLeadTime() {
        if (lastExecutionNanos == -1) {
            lastExecutionNanos = System.nanoTime();
            if (millisecondLead >= 0) return true;
        }

        if (millisecondLead < 0) return false;
        if (millisecondLead == 0) return true;

        long currentNanos = System.nanoTime();
        long differenceNanos = currentNanos - lastExecutionNanos;
        long differenceMillis = differenceNanos / 1_000_000L;

        if (differenceMillis + 5 >= millisecondLead) { // Plus 5 milliseconds to account for possible slight undershoot
            lastExecutionNanos = System.nanoTime();
            return true;
        }

        return false;
    }
}
