package com.arsmeteorites.arsmeteorites.common.explode;

import java.util.ArrayList;
import java.util.List;

public class SphereShellCalculator {

    public static void main(String[] args) {
        int radius = 510;
        System.out.println("计算" + radius + "层球壳...");
        long startTime = System.currentTimeMillis();
        List<Integer> shellBlocks = new ArrayList<>();
        for (int r = 0; r <= radius; r++) shellBlocks.add(calculateShellBlocksOptimized(r));
        long endTime = System.currentTimeMillis();
        System.out.println("计算完成，耗时: " + (endTime - startTime) + " 毫秒");
        System.out.println("\n" + radius + "层球壳方块数量:");
        for (int i = 0; i < radius; i += 10) {
            for (int j = i; j < Math.min(i + 10, radius); j++) System.out.print(shellBlocks.get(j) + ", ");
            System.out.println();
        }
    }

    public static int calculateShellBlocksOptimized(int radius) {
        if (radius == 0) return 1;
        int blocksGenerated = 0;
        int radiusSq = radius * radius;
        int innerRadiusSq = (radius - 1) * (radius - 1);
        for (int x = 0; x <= radius; x++) {
            int xSq = x * x;
            if (xSq > radiusSq) continue;
            for (int y = 0; y <= radius; y++) {
                int ySq = y * y;
                int xySq = xSq + ySq;
                if (xySq > radiusSq) continue;
                int zMinSq = Math.max(0, innerRadiusSq - xySq + 1);
                int zMaxSq = radiusSq - xySq;
                if (zMinSq > zMaxSq) continue;
                int zMin = Math.max(0, (int) Math.sqrt(zMinSq));
                int zMax = Math.min(radius, (int) Math.sqrt(zMaxSq));
                while (zMin * zMin < zMinSq && zMin <= zMax) zMin++;
                while (zMax * zMax > zMaxSq && zMax >= zMin) zMax--;
                if (zMin > zMax) continue;
                int zCount = zMax - zMin + 1;
                int weight = 1;
                if (x != 0) weight *= 2;
                if (y != 0) weight *= 2;
                if (zMin == 0) {
                    if (zMax != 0) {
                        int zNonZeroCount = zCount - 1;
                        blocksGenerated += weight;
                        blocksGenerated += zNonZeroCount * weight * 2;
                        continue;
                    }
                } else weight *= 2;
                blocksGenerated += zCount * weight;
            }
        }
        return blocksGenerated;
    }
}
