package com.dyxiaojiazi.miningfatigue_reproduction;

import com.dyxiaojiazi.miningfatigue_reproduction.command.MiningFatigueCommand;
import com.dyxiaojiazi.miningfatigue_reproduction.config.ModConfig;
import com.dyxiaojiazi.miningfatigue_reproduction.event.MiningFatigueEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;

@Mod(MiningFatigueReproduction.MOD_ID)
public class MiningFatigueReproduction {
    public static final String MOD_ID = "miningfatigue_reproduction";

    public MiningFatigueReproduction() {
        // 注册配置文件
        ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.SPEC, MOD_ID + "-common.toml");

        // 注册事件处理器
        MinecraftForge.EVENT_BUS.register(new MiningFatigueEventHandler());

        // 注册命令
        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        MiningFatigueCommand.register(event.getDispatcher());
    }
}