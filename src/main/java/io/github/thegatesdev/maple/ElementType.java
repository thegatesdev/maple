package io.github.thegatesdev.maple;

public enum ElementType {
    MAP("dataMap", "a map element"),
    LIST("dataList", "a list element"),
    VALUE("dataValue", "a value element");

    private final String elementName, inlineName;

    ElementType(String elementName, String inlineName) {
        this.elementName = elementName;
        this.inlineName = inlineName;
    }


    public String getElementName() {
        return elementName;
    }

    public String getInlineName() {
        return inlineName;
    }
}
