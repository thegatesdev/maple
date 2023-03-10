# Maple

A simple type safe data structure.

## About

I made Maple as replacement for the Spigot Configuration API, to use in my own plugins that read a lot of data from the user.

## Usage

### Element data

Each DataElement has its element data.
Data consists of a *parent* and a *name*.
A DataElement's data can only be set **once**.

The element's **name** can be set in the constructor, or using the `DataElement#setName(String)` method.

The element's **parent** is not accessible to the outside. It is set by the parent when a child is added to its
structure. This is to prevent ghost items ( and also who are you to say who their parent is.. )

- `new DataMap()` This creates a new DataMap with its data *unset*.
- `new DataMap("Timmy")` This creates a new DataMap with its data *set*, the parent is defaulted to *null*.
- `new DataMap("Timmy").put("Johnny", new DataMap())` The DataMap put in the map named "Timmy" will have its name set to
  the key it is associated with ( in this case "Johnny" ).
- `new DataMap("Timmy").put("Johnny", new DataMap("JohnnyV2"))` This will fail. The DataMap we are putting in already
  has its data set!

### DataMap

*Key value pair structure*

```java
// Create map, giving it a name. This means this map cannot be a child anymore, as its data is set.
DataMap map = new DataMap("root_map");
// Add to map:
map.put("map_entry", new DataMap().put("nested_map_entry", new DataMap()));
map.put("integer_entry", new DataPrimitive(3));
// Get from map:
DataElement el = map.get("map_entry");
// Get specific primitive value from map:
int i = map.get("integer_entry", Integer.class);
// Find in map structure
DataElement nestedEntry = map.navigate("map_entry", "nested_map_entry");
// If you are very sure about what the element type is
DataList surelyThisIsAList = map.getList("map_entry"); // ElementException!!

// And more ..
```

### DataList

*Dynamic sized indexed structure*

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

// And more ..
```

### DataArray

*Static sized indexed structure*

```java
// Create array that can hold 20 elements.
DataArray array = new DataArray(20);
// Set elements:
array.set(0, new DataPrimitive("hello world"));
// Get elements:
DataElement element = array.get(0);

// And more ..
```

### DataPrimitive

*A single value*

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

// And more ..
```

### DataNull

*A null value*

DataNull gets returned by for example `DataMap#get(String)` if the element is not found. This enables the possibility
for type checking without the occasional NullPointerException.

### ElementException

This exception should mostly be used to notify the end user that they did something unexpected, for example when using assertion methods.