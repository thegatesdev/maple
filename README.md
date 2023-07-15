# Maple

A clean type safe configuration structure.

For *v3.0.0*

- [Usage](#usage)
  - [Creating elements](#creating-elements)
  - [Reading elements](#reading-elements)
  - [Checking elements](#checking-elements)
  - [Getting from elements](#getting-from-elements)
  - [The element itself](#the-element-itself)

## Usage

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
    else return null; // Null
});

// Viewing the items manually (the returned views are unmodifyable)
Map<String, DataElement> view = map.view();
List<DataElement> view = list.view();
```

### The element itself

An element, next to its value, also stores its key and parent (`key()`, `parent()`).
Any element can also be copied (deep copy) using `copy()`.
The `toString()` method is useful for laying out the structure of the children of the element.

Some other funny methods to check out (or more that I didn't feel like writing more):
- `isDescendant`
- `rootKey`
- `path`
- `nested`