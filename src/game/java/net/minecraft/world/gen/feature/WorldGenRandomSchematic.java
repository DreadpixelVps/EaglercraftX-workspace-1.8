package net.minecraft.world.gen.feature;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenRandomSchematic {
    private final WorldGenSchematic schematicGenerator;

    public WorldGenRandomSchematic(String resourcePath) {
        this.schematicGenerator = new WorldGenSchematic(resourcePath);
    }

    public void generateRandomly(World world, EaglercraftRandom random, int chunkX, int chunkZ) {
        if (random.nextInt(15) == 0) {
            int x = (chunkX * 16) + random.nextInt(16);
            int z = (chunkZ * 16) + random.nextInt(16);
            int y = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY();

            BlockPos spawnPos = new BlockPos(x, y, z);
            this.schematicGenerator.generate(world, random, spawnPos);
        }
    }
}