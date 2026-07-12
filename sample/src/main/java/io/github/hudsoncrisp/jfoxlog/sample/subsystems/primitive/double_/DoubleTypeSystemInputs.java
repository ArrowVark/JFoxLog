package io.github.hudsoncrisp.jfoxlog.sample.subsystems.primitive.double_;

import io.github.hudsoncrisp.jfoxlog.annotation.GenerateFoxgloveLoggable.GenerateFoxgloveLoggable;
import org.littletonrobotics.junction.AutoLog;

@AutoLog
@GenerateFoxgloveLoggable
public class DoubleTypeSystemInputs {
    public double scalar = 0d;
    public double[] array = new double[]{};
//    public List<Double> list = new ArrayList<>();
    public double null_;
}
