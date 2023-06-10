# Maple

A clean type safe configuration structure.

*v2.0.0*

## About

I personally created Maple for a couple of my Minecraft plugins as a replacement for the Spigot Configuration API.
The API is a bit clunky and doesn't have the features I need to read big amounts of complex configuration data, so I
decided it would be fun to try to create my own.

## Usage

### DataElement

The base class for any element in Maple is `DataElement`.
It stores its position in the structure, allowing you to get the root element, the path to this element and more.
It also has useful methods for checking its type, cloning and getting its value.

### Maple

The `Maple` class is used to create all types of elements.
In addition, it provides methods to 'read' elements from a plain object, Map, array etc;

```java
DataElement element = Maple.read(/* Anything */);

DataList list = Maple.readList("string_one", "string_two", "string_three"); // Varargs list

Map<String, List<?>> toBeRead = //...
DataMap map = Maple.readMap(toBeRead); // Read map
```

### DataValue

*A single value*

```java
// Create a static value
DataValue stringValue = Maple.value("Test"); // Holds string
DataValue intValue = Maple.value(2); // Holds integer

// Create a dynamic value
var random = new Random();
DataValue randomIntValue = Maple.value(Integer.class, random::nextInt);
// This generates a random integer every time the value is gotten
```

### DataMap

*String key, element value pair structure*

```java
// Create a map
DataMap map = Maple.map();
// With initial capacity
DataMap map = Maple.map(3);

// Modifying map
map.set("some_key", Maple.value(3));
map.remove("some_key");

// Getting elements
DataElement element = map.get("some_key");
DataElement nullableElement = map.getOrNull("some_other_key");

// Getting values
Integer intValue = map.getInt("some_key");
SomeObject obj = map.getUnsafe("some_other_key");
```

### DataList

*A list of elements*

```java
// Create a list
DataList list = Maple.list();
// With initial capacity
DataList map = Maple.list(3);

// Modifying list
list.add(Maple.value(3));
list.set(0, Maple.value(2));
list.remove(0);

// Getting elements
DataElement element = list.get(0);
DataElement nullableElement = list.getOrNull(1);

// Getting values

Integer intValue = list.getInt(0);
SomeObject obj = map.getUnsafe(1);
```

## ToDo:

- Make ElementException nicer?
- Add tests
- Merge with MapleTree??