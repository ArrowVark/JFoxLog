package io.github.hudsoncrisp.jfoxlog.sample.subsystems.primitive.long_;

import io.github.hudsoncrisp.jfoxlog.annotation.GenerateFoxgloveLoggable.GenerateFoxgloveLoggable;
import org.littletonrobotics.junction.AutoLog;

@AutoLog
@GenerateFoxgloveLoggable
public class LongTypeSystemInputs {
    public long scalar = 0L;
    public long[] array = new long[]{};
//    public List<Long> list = new ArrayList<>();
    public long null_;
}
