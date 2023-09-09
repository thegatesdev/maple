# Maple

A clean type safe configuration structure.

For *3.3.0-beta*.

- [Usage](#usage)
  - [Creating elements](#creating-elements)
  - [Reading elements](#reading-elements)
  - [Checking elements](#checking-elements)
  - [Getting from elements](#getting-from-elements)
  - [The element itself](#the-element-itself)
  - [The "read" package](#the-read-package)
- [Why](#why)
- [Contribute](#contribute)

## Usage

Since Maple is public and open source, you can easily get it from Jitpack.
To include Maple in your project, head to https://jitpack.io/#thegatesdev/maple,
pick a version and follow the instructions for your build system.

### Creating elements

*How to make the stuffs*

Creation is mostly done through a basic constructor.
DataValue is an exception because it is abstract.

```java
// Map element
var map = new DataMap();
map = new DataMap(6); // With initial capacity

// List element
var list = new DataList();
list = new DataList(9); // With initial capacity

// Value element
var value = DataValue.of("hello world"); // String type
value = DataValue.of(3) // Integer type etc.

// Dynamic value element
Random random = new Random();
var dynValue = DataValue.of(Integer.class, random::nextInt); // Gives a random integer when accessed

// Null element
var nothing = new DataNull();
```

### Reading elements

*How to generate the stuffs*

Reading is done through the Maple class.

```java
DataElement el = Maple.read(...); // Read anything, see the javadocs
        
// Read from array
list = Maple.readList("a", "b", "c");
// Read from a Java List
list = Maple.readList(List.of("a", "b", "c"));
// Read from any iterable
list = Maple.readList(Set.of("a", "b", "c")); // For example a set

// Read from a Java Map (ignores non String keys)
map = Maple.readMap(Map.of("a", 1, "b", 2));
```

### Checking elements

*How to know the stuffs*

```java
// Checking type
element.isList();
element.isMap();
// Using the class        
element.isOf(DataValue.class)
        
element.ifMap(map -> print("this is a map"), () -> print("this is not a map"));

// Checking value element type
value.valueOf(Integer.class);
```

### Modifying elements

*How to change the stuffs*

#### DataMap

```java
// Setting
map.set("key", DataValue.of("hello world")); // Using an element as input
map.set("key", "hello world"); // Or using a plain object (uses Maple.read() under the hood)

// Removing
var removed = map.remove("key");
removed.isValue(); // True

// Clearing
map.clear();
```

#### DataList

```java
// Adding
list.add(DataValue.of("hello world")); // Using an element as input
list.add("hello world"); // Or using a plain object (uses Maple.read() under the hood)

// Setting
list.set(0, DataValue.of("hello universe")); // Same story
list.set(0, "hello universe");

// Removing
var removed = list.remove(0);
removed.isValue(); // True
  
  // Clearing
list.clear();
```

### Getting from elements

*How to obtain the stuffs*

Since DataList and DataMap both implement MappedElements,
they share the easy ways of getting values, be it by String or Integer.

```java
var el = map.getOrNull("key");
DataValue<?> value = map.getValue("key"); // Throws if not found
value = map.getValue("key", DataValue.of("hello world")); // Default if not found
map.ifValue("key", value -> print("valueWasFound"));
// ... same for other element types (getMap, ifList etc.)

// Getting the actual Objects
String s = map.getObject("key", String.class);
s = map.getObject("key", String.class, "your fault");
// .. unsafely
s = map.getUnsafe("key");
s = map.getUnsafe("key", "de fault");
// HOLD ON, I spent more time than you thought:
s = map.getString("key");

// Getting from the dataValue itself
s = value.valueOr(String.class, "bye world");
s = value.valueOrThrow(String.class);
// .. and more!

// Crawling through descendants
map.crawl(element -> print(element + " is a descendant of this map"));
// Crawl and replace
map.crawl(element -> {
    if (element.isValue()) return DataValue.of("your data has been compromised");
    else return null; // Null keeps the original value in place
});

// Viewing the items manually (the returned views are unmodifyable)
Map<String, DataElement> view = map.view();
List<DataElement> view = list.view();
```

### The element itself

#### Parent and key

An element also stores its key and parent (`key()`, `parent()`).
The parent sets these when inserted into the structure. 
You can use `rootKey()` to set the key of an element without a parent.

#### Utilities

Any element can also be copied (deep copy) using `copy()`.
The `toString()` method is useful for laying out the structure of the element's children.

Some other funny methods to check out (or more that I didn't feel like writing more):
- `isDescendant`
- `path`
- `nested`

### The 'read' package

The classes in the 'read' package are merged to Maple from MapleTree.
The most important ones are DataType, Readable and ReadableOptions.

- A `DataType` defines a way to 'read' an element to another.
- A `Readable` is a DataType implementation using a supplied Function.
- `Options` and `Options.Builder` allow for defining map parameters, that can then be read from a DataMap.

I'll add documentation on these parts asap.

## Why

For me, Maple is a replacement / improvement to the SpigotMC Configuration API,
commonly used in Minecraft plugins. The Configuration API is quite broken and slow, and does not at all fit my needs.

## Contribute

Please submit a pull request if you feel like it!
If something isn't clear in this documentation, please open an issue.

## The bottom

Congrats, you've reached the bottom of this tiny documentation. Go buy a flower for someone.