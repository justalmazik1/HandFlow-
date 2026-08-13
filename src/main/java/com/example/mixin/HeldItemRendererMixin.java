package com.example.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    @Inject(
        method = "renderFirstPersonItem",
        at = @At("HEAD")
    )
    private void onRenderFirstPersonItem(
        AbstractClientPlayerEntity player,
        float tickDelta,
        float pitch,
        Hand hand,
        float swingProgress,
        ItemStack item,
        float equipProgress,
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        int light,
        CallbackInfo ci
    ) {
        if (player == null || MinecraftClient.getInstance().player == null) {
            return;
        }

        PlayerEntityRenderer renderer = (PlayerEntityRenderer) MinecraftClient.getInstance()
            .getEntityRenderDispatcher()
            .getRenderer(player);

        matrices.push();
        try {
            renderer.renderRightArm(matrices, vertexConsumers, light, player);
            renderer.renderLeftArm(matrices, vertexConsumers, light, player);
        } finally {
            matrices.pop();
        }
    }
}
