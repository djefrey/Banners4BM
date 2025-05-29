package dev.nincodedo.banners4bm;

import de.bluecolored.bluemap.api.BlueMapAPI;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Banners4BM
{
    public static final String MOD_ID = "banners4bm";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static Banners4BM INSTANCE;

    private final BannerMarkerManager bannerMarkerManager;
    private final BannerMapIcons bannerMapIcons;

    private Banners4BM()
    {
        bannerMarkerManager = new BannerMarkerManager();
        bannerMapIcons = new BannerMapIcons();
    }

    public static void init()
    {
        INSTANCE = new Banners4BM();
    }

    public static Banners4BM getInstance()
    {
        return INSTANCE;
    }

    public void onServerStart(MinecraftServer server)
    {
        BlueMapAPI.onEnable(blueMapAPI ->
        {
            LOGGER.info("Starting Banners4BM");
            bannerMarkerManager.loadMarkers(server.overworld());
            bannerMapIcons.loadMapIcons(blueMapAPI);
        });

        BlueMapAPI.onDisable(blueMapAPI ->
        {
            LOGGER.info("Stopping Banners4BM");
            bannerMarkerManager.saveMarkers();
        });
    }

    public void onBlockEntityInteract(Player player, InteractionHand hand, BlockEntity be, Level level)
    {
        if (!level.dimension().equals(Level.OVERWORLD) || player.isSpectator())
        {
            return;
        }

        ItemStack item = player.getItemInHand(hand);

        if (!item.is(Items.FILLED_MAP) || !be.getBlockState().is(BlockTags.BANNERS))
        {
            return;
        }

        LOGGER.trace("Toggling marker at {}", be.getBlockPos());
        bannerMarkerManager.toggleMarker(be, true);
    }

    public void onBlockEntityDestroy(Player player, BlockEntity be, Level level)
    {
        if (level.dimension().equals(Level.OVERWORLD) && be.getBlockState().is(BlockTags.BANNERS))
        {
            bannerMarkerManager.removeMarker(be);
        }
    }
}
