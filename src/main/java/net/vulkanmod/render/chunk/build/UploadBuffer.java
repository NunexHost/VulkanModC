package net.vulkanmod.render.chunk.build;

import net.vulkanmod.render.chunk.util.Util;
import net.vulkanmod.render.vertex.TerrainBufferBuilder;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public class UploadBuffer {

    public final int indexCount;
    public final boolean autoIndices;
    public final boolean indexOnly;
    private ByteBuffer vertexBuffer;
    private ByteBuffer indexBuffer;

    //debug
    private boolean released = false;

    public UploadBuffer(TerrainBufferBuilder.RenderedBuffer renderedBuffer) {
        TerrainBufferBuilder.DrawState drawState = renderedBuffer.drawState();
        this.indexCount = drawState.indexCount();
        this.autoIndices = drawState.sequentialIndex();
        this.indexOnly = drawState.indexOnly();

        // Avoid unnecessary copying - directly reference buffers if possible
        if (!this.indexOnly) {
            this.vertexBuffer = renderedBuffer.vertexBuffer();
        } else {
            this.vertexBuffer = null;
        }

        if (!drawState.sequentialIndex()) {
            this.indexBuffer = renderedBuffer.indexBuffer();
        } else {
            this.indexBuffer = null;
        }
    }

    public int indexCount() {
        return indexCount;
    }

    public ByteBuffer getVertexBuffer() {
        // Ensure caller isn't modifying referenced buffer
        return vertexBuffer != null ? vertexBuffer.asReadOnlyBuffer() : null;
    }

    public ByteBuffer getIndexBuffer() {
        // Ensure caller isn't modifying referenced buffer
        return indexBuffer != null ? indexBuffer.asReadOnlyBuffer() : null;
    }

    public void release() {
        // Only free buffers if they weren't directly referenced from source
        if (vertexBuffer != renderedBuffer.vertexBuffer()) {
            MemoryUtil.memFree(vertexBuffer);
        }
        if (indexBuffer != renderedBuffer.indexBuffer()) {
            MemoryUtil.memFree(indexBuffer);
        }
        this.released = true;
    }
}
