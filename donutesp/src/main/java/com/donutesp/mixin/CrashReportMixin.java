package com.donutesp.mixin;

import net.minecraft.util.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CrashReport.class)
public class CrashReportMixin {

    @ModifyVariable(method = "asString", at = @At("RETURN"), ordinal = 0)
    private String stripOurPackage(String report) {
        String[] lines = report.split("\n");
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            if (!line.contains("com.donutesp"))
                sb.append(line).append('\n');
        }
        return sb.toString();
    }
}
