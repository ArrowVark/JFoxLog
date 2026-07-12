package io.github.hudsoncrisp.jfoxlog.sample.subsystems.primitive.string;

import io.github.hudsoncrisp.jfoxlog.annotation.GenerateFoxgloveLoggable.GenerateFoxgloveLoggable;
import org.littletonrobotics.junction.AutoLog;

@AutoLog
@GenerateFoxgloveLoggable
public class StringTypeSystemInputs {
    public String scalar = "";
    public String[] array = new String[]{};
//    public List<String> list = new ArrayList<>();
    public String null_;
}
