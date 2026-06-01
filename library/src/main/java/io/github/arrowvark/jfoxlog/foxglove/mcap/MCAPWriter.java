package io.github.arrowvark.jfoxlog.foxglove.mcap;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class MCAPWriter {
    private static final ByteBuffer magicBytes = ByteBuffer
            .allocate(20)
            .order(ByteOrder.LITTLE_ENDIAN)
            .putInt(0x89)
            .putChar('M')
            .putChar('C')
            .putChar('A')
            .putChar('P')
            .putInt(0x30)
            .putChar('\r')
            .putChar('\n')
            .flip().asReadOnlyBuffer();
    private static final int headerOp = 0x01;
    private static final int footerOp = 0x02;
    private static final int schemaOp = 0x03;
    private static final int channelOp = 0x04;
    private static final int messageOp = 0x05;
    private static final int chunkOp = 0x06;
    private static final int messageIndexOp = 0x07;
    private static final int chunkIndexOp = 0x08;
    private static final int attachmentOp = 0x09;
    private static final int metadataOp = 0x0C;
    private static final int dataEndOp = 0x0F;
    private static final int attachmentIndexOp = 0x0A;
    private static final int metadataIndexOp = 0x0D;
    private static final int statisticsOp = 0x0B;
    private static final int summaryOffsetOp = 0x0E;

    private static final ByteBuffer header;
    static {
        String library = "prime-foxglove";
        header = constructRecord(headerOp,
                ByteBuffer
                        .allocate(4 + 4 + library.length())
                        .order(ByteOrder.LITTLE_ENDIAN)
                        .position(3)
                        .put(library.getBytes())
                        .flip()
                );
    }

    private static final List<ByteBuffer> registry = new ArrayList<>();
    private static int fileSize;

    private static ByteBuffer constructRecord(int op, ByteBuffer message) {
        int length = message.position();
        int size = 4 + 4 + length;
        fileSize += size;

        ByteBuffer buffer = ByteBuffer
                .allocate(size)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(op)
                .putInt(length)
                .put(message)
                .flip();
        registry.add(buffer);
        return buffer;
    }

    private static ByteBuffer constructFile() {
        ByteBuffer buffer = ByteBuffer
                .allocate(fileSize)
                .put(magicBytes)
                .put(header);

        for (ByteBuffer buff : registry) {
            buffer.put(buff);
        }

        return buffer;
    }
}
