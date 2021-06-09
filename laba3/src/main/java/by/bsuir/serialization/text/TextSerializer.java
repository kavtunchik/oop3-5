package by.bsuir.serialization.text;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;

public class TextSerializer implements Closeable {
    private final Writer writer;

    public TextSerializer(Writer writer) {
        this.writer = writer;
    }

    public void write(TextSerializable[] items) throws IOException {
        for (TextSerializable item : items) {
            writer.write(item.textSerialize() + "\n");
        }
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}
