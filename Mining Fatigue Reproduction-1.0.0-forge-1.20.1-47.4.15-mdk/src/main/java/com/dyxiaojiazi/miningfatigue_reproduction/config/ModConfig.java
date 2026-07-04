package com.dyxiaojiazi.miningfatigue_reproduction.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {
    public static final ForgeConfigSpec SPEC;
    public static final Config CONFIG;

    static {
        final Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config::new);
        SPEC = specPair.getRight();
        CONFIG = specPair.getLeft();
    }

    public static class Config {
        // 挖掘减速倍率（陆/水）
        public final ForgeConfigSpec.DoubleValue speedDivisorLand;
        public final ForgeConfigSpec.DoubleValue speedDivisorWater;

        // 移速削弱开关及百分比
      /*  public final ForgeConfigSpec.BooleanValue enableMoveSlowdown;
        public final ForgeConfigSpec.DoubleValue moveSlowdownPercent;
*/
        // 攻击速度削弱开关及百分比
      /*  public final ForgeConfigSpec.BooleanValue enableAttackSlowdown;
        public final ForgeConfigSpec.DoubleValue attackSlowdownPercent;
*/
        // 水下持续时间延长开关及百分比
        public final ForgeConfigSpec.BooleanValue enableUnderwaterExtension;
        public final ForgeConfigSpec.DoubleValue underwaterExtensionPercent;

        Config(ForgeConfigSpec.Builder builder) {
            builder.push("general");

            speedDivisorLand = builder
                    .comment("陆地上挖掘疲劳减速倍数（速度变为 1/此值），默认 3")
                    .defineInRange("speedDivisorLand", 3.0, 1.0, 100.0);

            speedDivisorWater = builder
                    .comment("水下挖掘疲劳减速倍数（速度变为 1/此值），默认 3")
                    .defineInRange("speedDivisorWater", 3.0, 1.0, 100.0);

           /* enableMoveSlowdown = builder
                    .comment("是否启用移动速度削弱")
                    .define("enableMoveSlowdown", true);

            moveSlowdownPercent = builder
                    .comment("移动速度削弱百分比（0~1），默认 0.15（15%）")
                    .defineInRange("moveSlowdownPercent", 0.15, 0.0, 1.0);

            enableAttackSlowdown = builder
                    .comment("是否启用攻击速度削弱")
                    .define("enableAttackSlowdown", true);

            attackSlowdownPercent = builder
                    .comment("攻击速度削弱百分比（0~1），默认 0.10（10%）")
                    .defineInRange("attackSlowdownPercent", 0.10, 0.0, 1.0);
*/
            enableUnderwaterExtension = builder
                    .comment("是否启用水下挖掘疲劳持续时间延长")
                    .define("enableUnderwaterExtension", true);

            underwaterExtensionPercent = builder
                    .comment("水下持续时间延长百分比（0~1），默认 0.50（50%）")
                    .defineInRange("underwaterExtensionPercent", 0.50, 0.0, 1.0);

            builder.pop();
        }
    }
}