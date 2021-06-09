package by.bsuir.serialization.xml;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;

public class ObjectXmlSerializer implements XmlSerializer {

    @Override
    public byte[] xmlSerialize(Object input) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLEncoder xmlEncoder = new XMLEncoder(outputStream);
        // Запись
        xmlEncoder.writeObject(input);
        xmlEncoder.close();
        // Результат
        return outputStream.toByteArray();
    }
}
