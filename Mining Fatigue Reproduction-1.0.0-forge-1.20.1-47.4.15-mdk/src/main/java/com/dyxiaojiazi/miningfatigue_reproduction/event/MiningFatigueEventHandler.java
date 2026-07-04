package com.dyxiaojiazi.miningfatigue_reproduction.event;

import com.dyxiaojiazi.miningfatigue_reproduction.config.ModConfig;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class MiningFatigueEventHandler {
    private static final UUID MOVE_SPEED_MODIFIER_UUID = UUID.fromString("f1a2b3c4-d5e6-7890-1234-567890abcdef");
    private static final UUID ATTACK_SPEED_MODIFIER_UUID = UUID.fromString("a1b2c3d4-e5f6-7890-1234-567890abcdef");

    private static final ThreadLocal<Boolean> processingPotion = ThreadLocal.withInitial(() -> false);

    // ----- 挖掘速度修正（核心） -----
    @SubscribeEvent
    public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (player.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            MobEffectInstance effect = player.getEffect(MobEffects.DIG_SLOWDOWN);
            int amplifier = effect.getAmplifier();
            int level = amplifier + 1;
            double originalFactor = Math.pow(0.3, level);

            float originalSpeed = event.getOriginalSpeed();
            double divisor = player.isUnderWater() ?
                    ModConfig.CONFIG.speedDivisorWater.get() :
                    ModConfig.CONFIG.speedDivisorLand.get();

            float newSpeed = (float) (originalSpeed / originalFactor / divisor);
            event.setNewSpeed(newSpeed);
        }
    }

    // ----- 移速 & 攻速削弱（每 tick 维护） -----
    /*@SubscribeEvent
    public void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player player)) return;

        boolean hasEffect = player.hasEffect(MobEffects.DIG_SLOWDOWN);

        // 移动速度
        if (ModConfig.CONFIG.enableMoveSlowdown.get()) {
            AttributeInstance moveSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);
            if (moveSpeed != null) {
                AttributeModifier mod = moveSpeed.getModifier(MOVE_SPEED_MODIFIER_UUID);
                if (hasEffect) {
                    if (mod == null) {
                        double percent = ModConfig.CONFIG.moveSlowdownPercent.get();
                        AttributeModifier newMod = new AttributeModifier(
                                MOVE_SPEED_MODIFIER_UUID,
                                "MiningFatigueMoveSlowdown",
                                -percent,
                                AttributeModifier.Operation.MULTIPLY_BASE
                        );
                        moveSpeed.addTransientModifier(newMod);
                    }
                } else {
                    if (mod != null) {
                        moveSpeed.removeModifier(MOVE_SPEED_MODIFIER_UUID);
                    }
                }
            }
        }

        // 攻击速度
        if (ModConfig.CONFIG.enableAttackSlowdown.get()) {
            AttributeInstance attackSpeed = player.getAttribute(Attributes.ATTACK_SPEED);
            if (attackSpeed != null) {
                AttributeModifier mod = attackSpeed.getModifier(ATTACK_SPEED_MODIFIER_UUID);
                if (hasEffect) {
                    if (mod == null) {
                        double percent = ModConfig.CONFIG.attackSlowdownPercent.get();
                        AttributeModifier newMod = new AttributeModifier(
                                ATTACK_SPEED_MODIFIER_UUID,
                                "MiningFatigueAttackSlowdown",
                                -percent,
                                AttributeModifier.Operation.MULTIPLY_BASE
                        );
                        attackSpeed.addTransientModifier(newMod);
                    }
                } else {
                    if (mod != null) {
                        attackSpeed.removeModifier(ATTACK_SPEED_MODIFIER_UUID);
                    }
                }
            }
        }
    }*/

    // ----- 水下时长延长（使用 MobEffectEvent.Added） -----
    @SubscribeEvent
    public void onPotionAdded(MobEffectEvent.Added event) {
        if (processingPotion.get()) return;
        if (!ModConfig.CONFIG.enableUnderwaterExtension.get()) return;

        LivingEntity entity = event.getEntity();
        MobEffectInstance effect = event.getEffectInstance();
        if (effect == null) return;

        if (effect.getEffect() == MobEffects.DIG_SLOWDOWN && entity.isUnderWater()) {
            int duration = effect.getDuration();
            if (duration <= 0) return;

            double extension = ModConfig.CONFIG.underwaterExtensionPercent.get();
            int newDuration = (int) (duration * (1 + extension));
            if (newDuration == duration) return;

            processingPotion.set(true);
            event.setCanceled(true); // 取消原始添加

            MobEffectInstance newEffect = new MobEffectInstance(
                    effect.getEffect(),
                    newDuration,
                    effect.getAmplifier(),
                    effect.isAmbient(),
                    effect.isVisible(),
                    effect.showIcon()
            );
            entity.addEffect(newEffect);

            processingPotion.set(false);
        }
    }
}