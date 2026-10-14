package alabaster.hearthandharvest.common.worldgen.structure.corn_maze;

import net.minecraft.util.RandomSource;

public class MazeGenerator {

    public static boolean[][] generate(int w, int h, RandomSource rand) {
        boolean[][] maze = new boolean[w][h];

        for (int x = 0; x < w; x++)
            for (int z = 0; z < h; z++)
                maze[x][z] = true;

        carve(1, 1, 0, 0, maze, rand);

        for (int x = 1; x < w - 1; x++) {
            for (int z = 1; z < h - 1; z++) {
                if (!maze[x][z]) continue;
                int open = 0;
                if (!maze[x - 1][z]) open++;
                if (!maze[x + 1][z]) open++;
                if (!maze[x][z - 1]) open++;
                if (!maze[x][z + 1]) open++;
                if (open >= 2 && rand.nextFloat() < 0.05f) maze[x][z] = false;
            }
        }

        int cx = w / 2;
        int cz = h / 2;
        for (int dx = -1; dx <= 1; dx++)
            for (int dz = -1; dz <= 1; dz++)
                maze[cx + dx][cz + dz] = false;

        maze[0][h / 2] = false;
        maze[w - 1][h / 2] = false;

        return maze;
    }

    private static void carve(int x, int z, int lastDx, int lastDz, boolean[][] maze, RandomSource rand) {
        int w = maze.length;
        int h = maze[0].length;
        int[] dirs = {0, 1, 2, 3};
        shuffle(dirs, rand);

        if ((lastDx != 0 || lastDz != 0) && rand.nextFloat() < 0.5f) {
            for (int i = 1; i < dirs.length; i++) {
                int dx = dirs[i] == 0 ? 2 : dirs[i] == 1 ? -2 : 0;
                int dz = dirs[i] == 2 ? 2 : dirs[i] == 3 ? -2 : 0;
                if (dx == lastDx && dz == lastDz) {
                    int tmp = dirs[0]; dirs[0] = dirs[i]; dirs[i] = tmp;
                    break;
                }
            }
        }

        for (int dir : dirs) {
            int dx = dir == 0 ? 2 : dir == 1 ? -2 : 0;
            int dz = dir == 2 ? 2 : dir == 3 ? -2 : 0;
            int nx = x + dx;
            int nz = z + dz;

            if (nx > 0 && nz > 0 && nx < w - 1 && nz < h - 1 && maze[nx][nz]) {
                maze[nx][nz] = false;
                maze[x + dx / 2][z + dz / 2] = false;
                carve(nx, nz, dx, dz, maze, rand);
            }
        }
    }

    private static void shuffle(int[] arr, RandomSource rand) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }
}