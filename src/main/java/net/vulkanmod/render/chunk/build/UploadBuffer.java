package net.vulkanmod.render.chunk.build;

import net.vulkanmod.render.chunk.util.Util;
import net.vulkanmod.render.vertex.TerrainBufferBuilder;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class UploadBuffer {

    public final int indexCount;
    public final boolean autoIndices;
    public final boolean indexOnly;
    private final ByteBuffer buffer;

    //debug
    private boolean released = false;

    public UploadBuffer(TerrainBufferBuilder.RenderedBuffer renderedBuffer) {
        TerrainBufferBuilder.DrawState drawState = renderedBuffer.drawState();
        this.indexCount = drawState.indexCount();
        this.autoIndices = drawState.sequentialIndex();
        this.indexOnly = drawState.indexOnly();

        if (!this.indexOnly) {
            this.buffer = Util.createDirectBuffer(renderedBuffer.vertexBuffer().capacity());
            buffer.order(ByteOrder.nativeOrder());
            buffer.put(renderedBuffer.vertexBuffer());
        } else {
            this.buffer = null;
        }

        if (!drawState.sequentialIndex()) {
            this.buffer = Util.appendDirectBuffer(this.buffer, renderedBuffer.indexBuffer());
        }
    }

    public int indexCount() { return indexCount; }

    public ByteBuffer getBuffer() { return buffer; }

    public void release() {
        if (buffer != null) {
            MemoryUtil.memFree(buffer);
        }
        this.released = true;
    }
}
