package net.minecraft.server;

import net.minecraft.world.World;
import net.minecraft.util.BlockPos;

public abstract class Structure {
    public abstract double getSpawnChance();
    public abstract void generate(World world, BlockPos origin);
}