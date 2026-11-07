package com.donutesp.mixin;

import com.donutesp.ModHider;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import io.netty.buffer.Unpooled;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.charset.StandardCharsets;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    private static final Identifier BRAND = new Identifier("minecraft", "brand");
    private boolean brandSent = false;

    @Inject(method = "sendImmediately", at = @At("HEAD"), cancellable = true)
    private void interceptBrand(Packet<?> packet, CallbackInfo ci) {
        if (brandSent) return;
        if (!(packet instanceof CustomPayloadC2SPacket pay)) return;
        if (!BRAND.equals(pay.channel())) return;

        ci.cancel();
        brandSent = true;

        byte[] brand = ModHider.FAKE_BRAND.getBytes(StandardCharsets.UTF_8);
        byte[] buf   = new byte[brand.length + 1];
        buf[0] = (byte) brand.length;
        System.arraycopy(brand, 0, buf, 1, brand.length);

        PacketByteBuf wrapped = new PacketByteBuf(Unpooled.wrappedBuffer(buf));
        ((ClientConnection)(Object)this).send(new CustomPayloadC2SPacket(BRAND, wrapped));
    }
}
