package net.vulkanmod.render.chunk.build;

import net.vulkanmod.render.chunk.util.Util;
import net.vulkanmod.render.vertex.TerrainBufferBuilder;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public class UploadBuffer {

    /**
     * Número de índices no buffer.
     */
    public final int indexCount;

    /**
     * Se os índices são gerados sequencialmente.
     */
    public final boolean autoIndices;

    /**
     * Se o buffer contém apenas índices.
     */
    public final boolean indexOnly;

    /**
     * Buffer de vértices.
     */
    private ByteBuffer vertexBuffer;

    /**
     * Buffer de índices.
     */
    private ByteBuffer indexBuffer;

    /**
     * Indica se o buffer já foi liberado.
     */
    private boolean released;

    /**
     * Constrói um buffer de upload.
     *
     * @param renderedBuffer Buffer renderizado.
     */
    public UploadBuffer(TerrainBufferBuilder.RenderedBuffer renderedBuffer) {
        TerrainBufferBuilder.DrawState drawState = renderedBuffer.drawState();
        this.indexCount = drawState.indexCount();
        this.autoIndices = drawState.sequentialIndex();
        this.indexOnly = drawState.indexOnly();

        /**
         * Verifica se o buffer contém vértices.
         */
        if (!this.indexOnly) {
            this.vertexBuffer = Util.createCopy(renderedBuffer.vertexBuffer());
        } else {
            this.vertexBuffer = null;
        }

        /**
         * Verifica se o buffer contém índices.
         */
        if (!drawState.sequentialIndex()) {
            this.indexBuffer = Util.createCopy(renderedBuffer.indexBuffer());
        } else {
            this.indexBuffer = null;
        }
    }

    /**
     * Obtém o número de índices no buffer.
     *
     * @return Número de índices.
     */
    public int indexCount() {
        return this.indexCount;
    }

    /**
     * Obtém o buffer de vértices.
     *
     * @return Buffer de vértices.
     */
    public ByteBuffer getVertexBuffer() {
        if (this.vertexBuffer == null) {
            throw new IllegalStateException("O buffer de vértices não existe.");
        }
        return this.vertexBuffer;
    }

    /**
     * Obtém o buffer de índices.
     *
     * @return Buffer de índices.
     */
    public ByteBuffer getIndexBuffer() {
        if (this.indexBuffer == null) {
            throw new IllegalStateException("O buffer de índices não existe.");
        }
        return this.indexBuffer;
    }

    /**
     * Libera o buffer.
     */
    public void release() {
        if (this.vertexBuffer != null) {
            MemoryUtil.memFree(this.vertexBuffer);
        }
        if (this.indexBuffer != null) {
            MemoryUtil.memFree(this.indexBuffer);
        }
        this.released = true;
    }
}
