# Maple

A clean type safe configuration structure.

## About

I personally created Maple for a couple of my Minecraft plugins as a replacement for the Spigot Configuration API.
The API is a bit clunky and doesn't have the features I need to read big amounts of complex configuration data, so I
decided it would be fun to try to create my own.

## Usage

- [Elements](#elements)
    - [DataElement](#elements)
    - [The Maple class](#maple-class)
    - [DataValue](#datavalue)
    - [DataMap](#datamap)
    - [DataList](#datalist)

### Elements

#### DataElement

The base class for any element in Maple is `DataElement`.
It stores its position in the structure, allowing you to get the root element, the path to this element and more.
It also has useful methods for checking its type, cloning and getting its value.

#### Maple class

The `Maple` class is used to create all types of elements.
In addition, it provides methods to 'read' elements from a plain object, Map, array etc;

```
DataElement element = Maple.read(/* Anything */);

DataList list = Maple.readList("string_one", "string_two", "string_three"); // Varargs list

Map<String, List<?>> toBeRead = //...
DataMap map = Maple.readMap(toBeRead); // Read map
```

#### DataValue

*A single value*

```
// Create a static value
DataValue<String> stringValue = DataValue.of("Test"); // Holds string
DataValue<Integer> intValue = DataValue.of(2); // Holds integer

// Create a dynamic value
var random = new Random();
DataValue<Integer> randomIntValue = DataValue.of(Integer.class, random::nextInt);
// This generates a random integer every time the value is gotten
```

#### DataMap

*String key, element value pair structure*

```
// Create a map
DataMap map = new DataMap();
// With initial capacity
DataMap map = new DataMap(3);

// Modifying map
map.set("some_key", DataValue.of(3));
map.remove("some_key");

// Getting elements
DataElement element = map.get("some_key");
DataElement nullableElement = map.getOrNull("some_other_key");

// Getting values
Integer intValue = map.getInt("some_key");
SomeObject obj = map.getUnsafe("some_other_key");

// Iterate values
map.eachValue(value -> System.out.println(value.key()));
```

#### DataList

*A list of elements*

```
// Create a list
DataList list = new DataList();
// With initial capacity
DataList map = new DataList(3);

// Modifying list
list.add(DataValue.of(3));
list.set(0, DataValue.of(2));
list.remove(0);

// Getting elements
DataElement element = list.get(0);
DataElement nullableElement = list.getOrNull(1);

// Getting values

Integer intValue = list.getInt(0);
SomeObject obj = map.getUnsafe(1);

// Iterate values
list.each(value -> System.out.println(value.key()));
```