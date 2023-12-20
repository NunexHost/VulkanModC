package net.vulkanmod.render.chunk.build;

import net.vulkanmod.render.chunk.util.Util;
import net.vulkanmod.render.vertex.TerrainBufferBuilder;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public class UploadBuffer {

    public final int indexCount;
    public final boolean autoIndices;
    public final boolean indexOnly;
    private final ByteBuffer vertexBuffer;
    private final ByteBuffer indexBuffer;

    //debug
    private boolean released = false;

    public UploadBuffer(TerrainBufferBuilder.RenderedBuffer renderedBuffer) {
        TerrainBufferBuilder.DrawState drawState = renderedBuffer.drawState();
        this.indexCount = drawState.indexCount();
        this.autoIndices = drawState.sequentialIndex();
        this.indexOnly = drawState.indexOnly();

        // Aloca buffers diretamente na memória de vídeo
        this.vertexBuffer = MemoryUtil.memAlloc(renderedBuffer.vertexBuffer().remaining());
        Util.copy(renderedBuffer.vertexBuffer(), this.vertexBuffer);

        if (!this.indexOnly) {
            this.indexBuffer = MemoryUtil.memAlloc(renderedBuffer.indexBuffer().remaining());
            Util.copy(renderedBuffer.indexBuffer(), this.indexBuffer);
        } else {
            this.indexBuffer = null;
        }
    }

    public int indexCount() { return indexCount; }

    public ByteBuffer getVertexBuffer() { return vertexBuffer; }

    public ByteBuffer getIndexBuffer() { return indexBuffer; }

    public void release() {
        // Libera buffers da memória de vídeo
        MemoryUtil.memFree(vertexBuffer);
        if (indexBuffer != null) {
            MemoryUtil.memFree(indexBuffer);
        }
        this.released = true;
    }
}
