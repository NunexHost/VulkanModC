package net.vulkanmod.render.vertex;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;

public class CustomVertexFormat {

    public static final VertexFormatElement ELEMENT_POSITION = new VertexFormatElement(0, VertexFormatElement.Type.BYTE, VertexFormatElement.Usage.POSITION, 3);
    public static final VertexFormatElement ELEMENT_COLOR = new VertexFormatElement(3, VertexFormatElement.Type.UBYTE, VertexFormatElement.Usage.COLOR, 4);
    public static final VertexFormatElement ELEMENT_UV0 = new VertexFormatElement(7, VertexFormatElement.Type.HALF_FLOAT, VertexFormatElement.Usage.UV, 2);

    public static final VertexFormat COMPRESSED_TERRAIN = new VertexFormat(
        new VertexFormatElement[] {
            ELEMENT_POSITION,
            ELEMENT_COLOR,
            ELEMENT_UV0
        }
    );

    private CustomVertexFormat() {}
}
