package io.github.hudsoncrisp.jfoxlog.sample.subsystems.primitive.enum_;

import io.github.hudsoncrisp.jfoxlog.annotation.GenerateFoxgloveLoggable.GenerateFoxgloveLoggable;
import org.littletonrobotics.junction.AutoLog;

@AutoLog
@GenerateFoxgloveLoggable
public class EnumTypeSystemInputs {
    public EnumTypeSystemValue scalar = EnumTypeSystemValue.ONE;
    public EnumTypeSystemValue[] array = new EnumTypeSystemValue[]{};
//    public List<EnumTypeSystemValue> list = new ArrayList<>();
    public EnumTypeSystemValue null_;
}
