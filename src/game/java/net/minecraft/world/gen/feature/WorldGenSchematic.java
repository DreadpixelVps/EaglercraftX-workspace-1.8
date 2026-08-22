package net.minecraft.world.gen.feature;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.io.InputStream;

public class WorldGenSchematic extends WorldGenerator {
    private final String resourcePath;

    public WorldGenSchematic(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
        try {
            // Load via class loader so TeaVM / browser client can read it from compiled assets
            InputStream in = WorldGenSchematic.class.getResourceAsStream(resourcePath);
            if (in == null) {
                System.err.println("[ScaryMod] Schematic resource not found: " + resourcePath);
                return false;
            }

            NBTTagCompound nbt = CompressedStreamTools.readCompressed(in);
            if (nbt == null) return false;

            short width = nbt.getShort("Width");
            short height = nbt.getShort("Height");
            short length = nbt.getShort("Length");

            byte[] blocks = nbt.getByteArray("Blocks");
            byte[] metadata = nbt.getByteArray("Data");

            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    for (int z = 0; z < length; ++z) {
                        int index = y * width * length + z * width + x;
                        int blockID = blocks[index] & 0xFF;
                        int meta = metadata[index] & 0xF;

                        Block block = Block.getBlockById(blockID);
                        if (block != null && block != Blocks.air) {
                            BlockPos targetPos = blockpos.add(x, y, z);
                            world.setBlockState(targetPos, block.getStateFromMeta(meta), 2);
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}