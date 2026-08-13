package com.example.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Shadow
    protected abstract void renderItem(float equipProgress, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, AbstractClientPlayerEntity player, int light);

    @Shadow
    protected abstract void renderArm(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, int light, AbstractClientPlayerEntity player, boolean showArm);

    @Inject(
        method = "renderFirstPersonItem",
        at = @At("HEAD"),
        cancellable = true
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
        if (player == null || hand == null) {
            return;
        }

        // Cancel the vanilla rendering
        ci.cancel();

        matrices.push();
        try {
            // Scale down the hand/item model
            matrices.scale(0.6f, 0.6f, 0.6f);

            // Translate to the lower right corner of the screen
            // X: positive = right, Y: negative = down, Z: negative = away from camera
            matrices.translate(0.8D, -0.5D, -0.7D);

            // Render the arm at this new position
            this.renderArm(matrices, (VertexConsumerProvider.Immediate) vertexConsumers, light, player, true);

            // Render the held item in the arm's hand
            this.renderItem(equipProgress, matrices, (VertexConsumerProvider.Immediate) vertexConsumers, player, light);
        } finally {
            matrices.pop();
        }
    }
}
