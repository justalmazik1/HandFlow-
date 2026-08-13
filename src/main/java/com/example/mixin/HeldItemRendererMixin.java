package com.example.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
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
    private void handflow$renderFirstPersonItem(
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

        if (hand == null) {
            return;
        }

        final boolean isMainHand = hand == Hand.MAIN_HAND;
        final boolean rightArm = player.getMainArm() == Arm.RIGHT;

        matrices.push();
        try {
            if ((isMainHand && rightArm) || (!isMainHand && !rightArm)) {
                matrices.translate(0.8D, -0.6D, 0.2D);
                matrices.scale(0.6f, 0.6f, 0.6f);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-75.0F));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(25.0F));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(10.0F));
                renderer.renderRightArm(matrices, vertexConsumers, light, player);
            } else {
                matrices.translate(-0.8D, -0.6D, 0.2D);
                matrices.scale(0.6f, 0.6f, 0.6f);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-75.0F));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-25.0F));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-10.0F));
                renderer.renderLeftArm(matrices, vertexConsumers, light, player);
            }
        } finally {
            matrices.pop();
        }
    }
}
