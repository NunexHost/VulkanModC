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

        if (!this.indexOnly) {
            this.vertexBuffer = Util.createCopy(renderedBuffer.vertexBuffer());
        } else {
            this.vertexBuffer = null;
        }

        if (!drawState.sequentialIndex()) {
            this.indexBuffer = Util.createCopy(renderedBuffer.indexBuffer());
        } else {
            this.indexBuffer = null;
        }

        // **Optimization:** Use a pool to avoid frequent memory allocation and deallocation.
        if (this.vertexBuffer != null) {
            this.vertexBuffer = VertexBufferPool.get().allocate();
        }
        if (this.indexBuffer != null) {
            this.indexBuffer = IndexBufferPool.get().allocate();
        }
    }

    public int indexCount() { return indexCount; }

    public ByteBuffer getVertexBuffer() { return vertexBuffer; }

    public ByteBuffer getIndexBuffer() { return indexBuffer; }

    public void release() {
        if (vertexBuffer != null) {
            VertexBufferPool.get().free(vertexBuffer);
        }
        if (indexBuffer != null) {
            IndexBufferPool.get().free(indexBuffer);
        }
        this.released = true;
    }
}
