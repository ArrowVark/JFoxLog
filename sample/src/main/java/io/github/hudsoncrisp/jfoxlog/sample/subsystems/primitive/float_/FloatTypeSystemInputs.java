package io.github.hudsoncrisp.jfoxlog.sample.subsystems.primitive.float_;

import io.github.hudsoncrisp.jfoxlog.annotation.GenerateFoxgloveLoggable.GenerateFoxgloveLoggable;
import org.littletonrobotics.junction.AutoLog;

@AutoLog
@GenerateFoxgloveLoggable
public class FloatTypeSystemInputs {
    public float scalar = 0f;
    public float[] array = new float[]{};
//    public List<Float> list = new ArrayList<>();
    public float null_;
}
