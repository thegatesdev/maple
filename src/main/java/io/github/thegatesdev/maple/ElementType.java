/*
Copyright 2023 Timar Karels

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package io.github.thegatesdev.maple;

/**
 * An enum defining the three types of elements found in Maple.
 */
public enum ElementType {
    /**
     * The map element type, corresponding to the {@link io.github.thegatesdev.maple.element.DataMap} class.
     */
    MAP("dataMap", "a map element"),
    /**
     * The list element type, corresponding to the {@link io.github.thegatesdev.maple.element.DataList} class.
     */
    LIST("dataList", "a list element"),
    /**
     * The value element type, corresponding to the {@link io.github.thegatesdev.maple.element.DataValue} interface.
     */
    VALUE("dataValue", "a value element");


    private final String elementName, inlineName;

    ElementType(String elementName, String inlineName) {
        this.elementName = elementName;
        this.inlineName = inlineName;
    }


    /**
     * @return the name of the element type
     */
    public String getElementName() {
        return elementName;
    }

    /**
     * @return the name of the element type as used in a sentence
     */
    public String getInlineName() {
        return inlineName;
    }
}
