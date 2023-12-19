package net.vulkanmod.render.chunk.build;

import net.vulkanmod.render.chunk.util.Util;
import net.vulkanmod.render.vertex.TerrainBufferBuilder;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

/**
 * Represents a buffer that has been uploaded to the GPU.
 */
public class UploadBuffer {

    /**
     * The number of indices in the buffer.
     */
    public int getIndexCount() { return indexCount; }

    /**
     * Returns a view of the vertex buffer.
     *
     * @throws IllegalStateException if the buffer has not been initialized
     */
    public ByteBuffer getVertexBufferView() {
        if (vertexBuffer == null) {
            throw new IllegalStateException("Vertex buffer has not been initialized");
        }
        return vertexBuffer.asReadOnlyBuffer();
    }

    /**
     * Returns a view of the index buffer.
     *
     * @throws IllegalStateException if the buffer has not been initialized
     */
    public ByteBuffer getIndexBufferView() {
        if (indexBuffer == null) {
            throw new IllegalStateException("Index buffer has not been initialized");
        }
        return indexBuffer.asReadOnlyBuffer();
    }

    /**
     * Creates a new `UploadBuffer` instance.
     *
     * @param renderedBuffer the rendered buffer to upload
     */
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
    }

    /**
     * Releases all resources associated with the buffer.
     */
    public void release() {
        if (vertexBuffer != null) {
            MemoryUtil.memFree(vertexBuffer);
        }
        if (indexBuffer != null) {
            MemoryUtil.memFree(indexBuffer);
        }
    }
}
