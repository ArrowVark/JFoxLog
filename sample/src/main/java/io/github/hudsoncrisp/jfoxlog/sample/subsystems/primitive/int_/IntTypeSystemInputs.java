package io.github.hudsoncrisp.jfoxlog.sample.subsystems.primitive.int_;

import io.github.hudsoncrisp.jfoxlog.annotation.GenerateFoxgloveLoggable.GenerateFoxgloveLoggable;
import org.littletonrobotics.junction.AutoLog;

@AutoLog
@GenerateFoxgloveLoggable
public class IntTypeSystemInputs {
    public int scalar = 0;
    public int[] array = new int[]{};
//    public List<Integer> list = new ArrayList<>();
    public int null_;
}
