package com.dyxiaojiazi.miningfatigue_reproduction.command;

import com.dyxiaojiazi.miningfatigue_reproduction.config.ModConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class MiningFatigueCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("miningfatigue")
                .then(Commands.literal("set")
                        .then(Commands.argument("divisor", DoubleArgumentType.doubleArg(1.0, 100.0))
                                .executes(ctx -> {
                                    double divisor = DoubleArgumentType.getDouble(ctx, "divisor");
                                    // 同时修改陆地和水中倍率（简单统一）
                                    ModConfig.CONFIG.speedDivisorLand.set(divisor);
                                    ModConfig.CONFIG.speedDivisorWater.set(divisor);
                                    ctx.getSource().sendSuccess(
                                            () -> Component.literal("已将挖掘疲劳减速倍数设为 " + divisor),
                                            true
                                    );
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("reload")
                        .executes(ctx -> {
                            // Forge 会自动监听配置文件变更，这里只做提示
                            ctx.getSource().sendSuccess(
                                    () -> Component.literal("配置文件已重新加载（部分变更可能需要重启生效）"),
                                    true
                            );
                            return 1;
                        })
                )
        );
    }
}