package io.github.hudsoncrisp.jfoxlog.foxglove.mcap;

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
    private static final byte headerOp = 0x01;
    private static final byte footerOp = 0x02;
    private static final byte schemaOp = 0x03;
    private static final byte channelOp = 0x04;
    private static final byte messageOp = 0x05;
    private static final byte chunkOp = 0x06;
    private static final byte messageIndexOp = 0x07;
    private static final byte chunkIndexOp = 0x08;
    private static final byte attachmentOp = 0x09;
    private static final byte metadataOp = 0x0C;
    private static final byte dataEndOp = 0x0F;
    private static final byte attachmentIndexOp = 0x0A;
    private static final byte metadataIndexOp = 0x0D;
    private static final byte statisticsOp = 0x0B;
    private static final byte summaryOffsetOp = 0x0E;

    private static final ByteBuffer header;
    static {
        String library = "JFoxLog";
        header = constructRecord(headerOp,
                ByteBuffer
                        .allocate(4 + 4 + library.length())
                        .order(ByteOrder.LITTLE_ENDIAN)
                        .position(3)
                        .put(library.getBytes())
                        .flip().asReadOnlyBuffer()
                );
    }

    private static final List<ByteBuffer> registry = new ArrayList<>();
    private static int fileSize;

    private static ByteBuffer constructRecord(byte op, ByteBuffer message) {
        int length = message.position();
        int size = 4 + 4 + length;
        fileSize += size;

        ByteBuffer buffer = ByteBuffer
                .allocate(size)
                .order(ByteOrder.LITTLE_ENDIAN)
                .put(op)
                .putInt(length)
                .put(message)
                .flip();
        registry.add(buffer);
        return buffer;
    }

    private static ByteBuffer constructFullBuffer() {
        ByteBuffer buffer = ByteBuffer
                .allocate(fileSize)
                .put(magicBytes)
                .put(header);

        for (ByteBuffer buff : registry) {
            buffer.put(buff);
        }

        return buffer;
    }

//    private static File constructFile() {
//        File file = new File("~/jfoxlog/logs/test.mcap");
//    }
}
