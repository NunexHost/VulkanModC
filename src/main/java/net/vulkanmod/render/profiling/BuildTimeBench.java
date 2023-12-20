package net.vulkanmod.render.profiling;

import net.minecraft.client.Minecraft;
import net.vulkanmod.render.chunk.build.TaskDispatcher;

public abstract class BuildTimeBench {

    private static boolean bench = false;
    private static long startTime;
    private static float benchTime;

    /**
     * Starts the benchmark timer.
     */
    public static void startBench() {
        bench = true;
        Minecraft.getInstance().levelRenderer.allChanged();
    }

    /**
     * Stops the benchmark timer and returns the elapsed time in seconds.
     *
     * @return The elapsed time in seconds.
     */
    public static float getBenchTime() {
        if (!bench) {
            return 0.0f;
        }

        float elapsedTime = (System.nanoTime() - startTime) * 0.000001f;
        bench = false;
        startTime = 0;

        return elapsedTime;
    }

    /**
     * Runs the benchmark timer.
     *
     * @param building Whether the benchmark is being run during the building phase.
     * @param taskDispatcher The task dispatcher.
     */
    public static void runBench(boolean building, TaskDispatcher taskDispatcher) {
        if (bench && !taskDispatcher.isIdle()) {
            benchTime = (System.nanoTime() - startTime) * 0.000001f;
            bench = false;
            startTime = 0;
        }
    }
}
