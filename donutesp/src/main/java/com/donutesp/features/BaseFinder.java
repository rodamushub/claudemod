package com.donutesp.features;

import net.minecraft.block.entity.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.*;

public class BaseFinder {

    public  boolean enabled   = false;
    private int     tickCount = 0;

    public record BaseCluster(BlockPos centroid, int score, List<BlockPos> blocks) {}

    private final List<BaseCluster> clusters = new ArrayList<>();

    public void tick(MinecraftClient mc) {
        if (!enabled || mc.world == null || mc.player == null) return;
        tickCount++;
        if (tickCount % 40 != 0) return;
        scan(mc);
    }

    private void scan(MinecraftClient mc) {
        Map<BlockPos, Integer> scoredBlocks = new HashMap<>();
        int viewDist = mc.options.getClampedViewDistance();
        ChunkPos center = mc.player.getChunkPos();

        for (int cx = center.x - viewDist; cx <= center.x + viewDist; cx++) {
            for (int cz = center.z - viewDist; cz <= center.z + viewDist; cz++) {
                WorldChunk chunk = mc.world.getChunkManager().getWorldChunk(cx, cz);
                if (chunk == null) continue;
                for (Map.Entry<BlockPos, BlockEntity> entry : chunk.getBlockEntities().entrySet()) {
                    int score = scoreBlockEntity(entry.getValue());
                    if (score > 0) scoredBlocks.put(entry.getKey().toImmutable(), score);
                }
            }
        }

        clusters.clear();
        clusters.addAll(clusterBlocks(scoredBlocks));
        clusters.sort(Comparator.comparingInt(BaseCluster::score).reversed());
    }

    private int scoreBlockEntity(BlockEntity be) {
        if (be instanceof ChestBlockEntity || be instanceof BarrelBlockEntity)    return 5;
        if (be instanceof ShulkerBoxBlockEntity)                                  return 8;
        if (be instanceof FurnaceBlockEntity || be instanceof SmokerBlockEntity)  return 3;
        if (be instanceof CraftingTableBlockEntity)                               return 2;
        if (be instanceof BedBlockEntity)                                         return 4;
        if (be instanceof EnchantingTableBlockEntity)                             return 6;
        if (be instanceof AnvilBlockEntity)                                       return 4;
        if (be instanceof BrewingStandBlockEntity)                                return 5;
        return 0;
    }

    private List<BaseCluster> clusterBlocks(Map<BlockPos, Integer> blocks) {
        List<BaseCluster> result = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();

        for (BlockPos seed : blocks.keySet()) {
            if (visited.contains(seed)) continue;

            List<BlockPos> cluster = new ArrayList<>();
            Queue<BlockPos> queue = new ArrayDeque<>();
            queue.add(seed);
            visited.add(seed);

            while (!queue.isEmpty()) {
                BlockPos cur = queue.poll();
                cluster.add(cur);
                for (BlockPos other : blocks.keySet()) {
                    if (visited.contains(other)) continue;
                    if (Math.sqrt(cur.getSquaredDistance(other)) <= 16.0) {
                        visited.add(other);
                        queue.add(other);
                    }
                }
            }

            if (cluster.size() < 3) continue;

            int totalScore = cluster.stream().mapToInt(p -> blocks.getOrDefault(p, 0)).sum();
            int cx = (int) cluster.stream().mapToInt(BlockPos::getX).average().orElse(0);
            int cy = (int) cluster.stream().mapToInt(BlockPos::getY).average().orElse(0);
            int cz = (int) cluster.stream().mapToInt(BlockPos::getZ).average().orElse(0);
            result.add(new BaseCluster(new BlockPos(cx, cy, cz), totalScore, cluster));
        }
        return result;
    }

    public List<BaseCluster> getClusters() { return clusters; }
    public void toggle() { enabled = !enabled; }
}
