package by.bsuir.serialization.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class CompressingXmlSerializer extends XmlSerializerDecorator {

    public CompressingXmlSerializer(XmlSerializer xmlSerializer) {
        super(xmlSerializer);
    }

    @Override
    public byte[] xmlSerialize(Object input) {
        try {
            byte[] bytes = super.xmlSerialize(input);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
            // Сжатие
            gzipOutputStream.write(bytes);
            gzipOutputStream.close();
            // Результат
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
