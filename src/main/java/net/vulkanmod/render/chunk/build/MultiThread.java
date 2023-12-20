package net.vulkanmod.render.chunk.build;

import java.util.concurrent.*;
import java.lang.Runnable;

public class MultiThread {

public void initMultithreading() {
  // Cria uma pool de threads.
  this.m_threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  // Divide as tarefas de renderização em várias threads.
  for (int i = 0; i < this.m_threadPool.getPoolSize(); i++) {
    this.m_threadPool.submit(() -> {
      // Renderiza uma parte da tela.
      RenderPartOfScreen();
    });
  }
}
