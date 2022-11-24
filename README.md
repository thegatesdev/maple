# Maple

A simple type safe data structure.

## Why

Minecraft server plugins.
I needed to read and use a lot of data read from ( yaml ) data files.
Spigot does have its own data structure, called the Configuration API,
which is pretty garbage for the code I wanted to write, so I started Maple.

The goal was to make a small, type safe structure with short syntax and a lot of utility / shortcut methods to write
less code and nested if statements. An example for getting a String value from a map:

```java
// Messy approach:
DataElement element = data.get("my_string_property");
if(element.isPrimitive()){
    DataPrimitive primitive = element.asPrimitive();
    if (primitive.valueOf(String.class)){
        String stringData = primitive.valueOrNull(String.class); // You could also use valueUnsafe()
        // Nice! We got the data we wanted! But what a mess me made!
        // Oh great, we also need to handle 2 'else' cases..
    }
}
// Can also be written as:
String stringData = data.get("my_string_property", String.class, "404 not found");
```

## How

### Data

Each DataElement has its data.
Data consists of a *parent* and a *name*.
A DataElement's data can only be set **once**.

The element's **name** can be set in the constructor ( Except for DataPrimitive ), or using
the `DataElement#setName(String)` method.

The element's **parent** is not accessible to the outside. It is set by the parent when a child is added to its
structure. This is to prevent ghost items ( and also who are you to say who their parent is.. )

- `new DataMap()` This creates a new DataMap with its data *unset*.
- `new DataMap("Timmy")` This creates a new DataMap with its data *set*, the parent is defaulted to *null*.
- `new DataMap("Timmy").put("Johnny", new DataMap())` The DataMap put in the map named "Timmy" will have its name set to
  the key it is associated with ( in this case "Johnny" ).
- `new DataMap("Timmy").put("Johhny", new DataMap("JohnnyV2"))` This will fail. The DataMap we are putting in already
  has its data set!

### DataMap

```java
// Create map, giving it a name. This means this map cannot be a child anymore, as its data is set.
DataMap map = new DataMap("root_map");
// Add to map:
map.put("map_entry", new DataMap().put("nested_map_entry", new DataMap()));
// Get from map:
DataElement el = map.get("map_entry");
// Find in map structure
DataElement nestedEntry = map.navigate("map_entry", "nested_map_entry");
// If you are very sure about what the element type is
DataList surelyThisIsAList = map.getList("map_entry"); // ElementException!!
```

### DataList

```java
// Create list, giving it no name. This means this list can be parented.
DataList list = new DataList();
// Add to list:
list.add(new DataList());
// Get from list:
DataElement el = list.get(0);
// Iterate over elements by type:
Iterator<DataMap> mapsInList = list.iterator(DataMap.class);
// Get a list of primitive elements values of a certain type:
List<String> stringPrimitives = list.primitiveList(String.class);
```

### DataPrimitive

```java
// Create primitive. A DataPrimitive is an exception in that there is no constructor with name parameter.
// This creates a DataPrimitive with its VALUE, NOT ITS NAME set to "hello world"
DataPrimitive primitive = new DataPrimitive("hello world");
// If you want to set the name, use the according method:
primitive.setName("root_primitive");
// You can do all sorts of checks
primitive.valueOf(String.class) // true
primitive.isNumberValue() // false
// Get value or throw
String stringValue = primitive.requireValue(String.class); // "hello world"
int intValue = primitive.requireValue(Integer.class); // ElementException with appropriate message.
```

### DataNull

Literally a *null* value. Who could have thought.

DataNull gets returned by for example `DataMap#get(String)` if the element is not found. This enables the possibility
for type checking without the occasional NullPointerException.