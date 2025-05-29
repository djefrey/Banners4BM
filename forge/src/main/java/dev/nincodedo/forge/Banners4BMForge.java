package dev.nincodedo.forge;

import dev.nincodedo.banners4bm.Banners4BM;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(Banners4BM.MOD_ID)
public final class Banners4BMForge
{
    public Banners4BMForge()
    {
        Banners4BM.init();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStart(ServerStartedEvent evt)
    {
        Banners4BM.getInstance().onServerStart(evt.getServer());
    }

    @SubscribeEvent
    public void onBlockInteract(PlayerInteractEvent.RightClickBlock evt)
    {
        Level level = evt.getLevel();

        if (level.isClientSide)
        {
            return;
        }

        BlockEntity be = level.getBlockEntity(evt.getPos());

        if (be != null)
        {
            Banners4BM.getInstance().onBlockEntityInteract(evt.getEntity(), evt.getHand(), be, level);
        }
    }

    @SubscribeEvent
    public void onBlockDestroy(BlockEvent.BreakEvent evt)
    {
        LevelAccessor accessor = evt.getLevel();

        if (accessor.isClientSide())
        {
            return;
        }

        if (accessor instanceof Level level)
        {
            BlockEntity be = level.getBlockEntity(evt.getPos());

            if (be != null)
            {
                Banners4BM.getInstance().onBlockEntityDestroy(evt.getPlayer(), be, level);
            }
        }
    }
}
