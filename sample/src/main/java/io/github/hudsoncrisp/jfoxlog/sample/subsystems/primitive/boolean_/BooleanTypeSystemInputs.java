package io.github.hudsoncrisp.jfoxlog.sample.subsystems.primitive.boolean_;

import io.github.hudsoncrisp.jfoxlog.annotation.GenerateFoxgloveLoggable.GenerateFoxgloveLoggable;
import org.littletonrobotics.junction.AutoLog;

@AutoLog
@GenerateFoxgloveLoggable
public class BooleanTypeSystemInputs {
    public boolean scalar = false;
    public boolean[] array = new boolean[]{};
//    public List<Boolean> list = new ArrayList<>();
    public boolean null_;
}
