package dev.nincodedo.fabric;

import dev.nincodedo.banners4bm.Banners4BM;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class Banners4BMFabric implements ModInitializer {
    @Override
    public void onInitialize()
    {
        Banners4BM.init();

        ServerLifecycleEvents.SERVER_STARTED.register(Banners4BM.getInstance()::onServerStart);

        UseBlockCallback.EVENT.register((player, level, hand, hitResult) ->
        {
            BlockEntity be =  level.getBlockEntity(hitResult.getBlockPos());

            if (be != null)
            {
                Banners4BM.getInstance().onBlockEntityInteract(player, hand, be, level);
            }

            return InteractionResult.PASS;
        });

        PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, be) ->
        {
            if (be != null)
            {
                Banners4BM.getInstance().onBlockEntityDestroy(player, be, level);
            }
        });
    }
}
