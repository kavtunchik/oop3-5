package by.bsuir.serialization.xml;

class XmlSerializerDecorator implements XmlSerializer {
    private final XmlSerializer xmlSerializer;

    public XmlSerializerDecorator(XmlSerializer xmlSerializer) {
        this.xmlSerializer = xmlSerializer;
    }

    @Override
    public byte[] xmlSerialize(Object input) {
        return xmlSerializer.xmlSerialize(input);
    }
}
