package by.bsuir.plugins;

import com.github.underscore.lodash.U;

public class XmlAndJson implements Plugin {
    private final U<?> u;

    public XmlAndJson(U<?> u) {
        this.u = u;
    }

    @Override
    public String saveTransform(String output) {
        return u.xmlToJson(output);
    }

    @Override
    public String loadTransform(String input) {
        return u.jsonToXml(input);
    }
}
