package io.github.hudsoncrisp.jfoxlog.debug;

public class FoxgloveDebugLogMeta {
    public enum Severity {
        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
        FATAL
    }

    public enum Tag {
        PRERUN,
        EXPECTED,
        UNRECOVERABLE,
    }
}
