# Maple

A simple, type safe configuration structure

*Updated for version 4.0.0*

## Contents

- [About](#about)
- [Usage](#usage)
  - [Including the library in your project](#including-in-your-project)
  - [Element basics](#element-basics)
  - [Element conversion](#conversion)
  - [Element crawling](#crawling)
  - [Applying options](#datatype-and-mapoptions)

## About

**Maple** was made as a replacement for the Spigot Configuration API,
as that API was quite limiting and ugly for my use cases.

My goal with this library is to make it easy to wrap configuration values
in a clean and understandable structure, providing plenty of utility methods
for specific scenario's.

Feel free to open an issue, or even a pull request, if you have any concerns or suggestions.

## Usage

### Including in your project

Since Maple is public on Github, you can easily include it from JitPack.
Just head to https://jitpack.io/#thegatesdev/maple, select a version and follow the instructions for your build system.

### Element Basics

**Creating elements**

Creating list, map and value types is as easy as calling their constructors;
```java
DataMap myMap = new DataMap();
DataList myList = new DataList();
DataValue<String> myImmutableValue = new StaticDataValue<>("hello world");
DataValue<String> myDynamicValue = new DynamicDataValue<>(String.class, () -> "hello world");
```

**Checking types**

There are multiple methods to check the type of an element itself;

```java
DataElement myElement = new DataMap();

myElement.isList(); // False
myElement.isMap(); // True

myElement.getType(); // ElementType.MAP

myElement.ifMap(map -> print("myElement is a map!"), () -> print("myElement is not a map!"));
```

**Modifying elements**

Simple setting and adding operation are currently supported.
More will be added in future releases, like removing and clearing.

```java
DataList myList = new DataList();

myList.add(new StaticDataValue<>("Hello"));
myList.add(new StaticDataValue<>("World"));
myList.set(1, new StaticDataValue<>("Universe"));

DataMap myMap = new DataMap();
myMap.set("Earth", new StaticDataValue<>("Humans"));
myMap.set("Mars", new StaticDataValue<>("Aliens"));
```

**Getting from elements**

```java
DataMap myMap = //.. Map with information

// Plain elements
DataElement otherElement = myMap.getOrThrow("somekey");
DataMap otherMap = myMap.getMap("mapkey");
DataList otherList = myMap.getList("listkey");

// Values
String stringValue = myMap.get("message", String.class);
String stringValue = myMap.get("message", String.class, "no message"); // With default
int intValue = myMap.getInt("count", 0);

myMap.each(element -> print("Found an element"));
```

### Conversion

Maple was meant to be used with data from other sources, e.g. json or yaml configuration files.

To convert plain Java objects to a Maple structure, for example the output from snakeyaml, use the `Conversion` class.
```java
Conversion conversion = new DefaultConversion();

Map<?,?> output = //.. e.g. load from file
DataMap data = conversion.convertMap(fileOutput);
```

This will convert the entire structure to a Maple representation.
The `DefaultConversion` implementation should be adequate for most use cases.

### Crawling

Every Maple element supports the 'crawl' operation. Crawling will visit every descendant of an element, and optionally replace it.

Crawling can be used to replace placeholder values to their actual values.
```java
DataMap map = //..
    
map.crawl(element -> {
    if (!element.isValue()) return Optional.empty(); // Don't do anything
    return Optional.of(new StaticDataValue<>("You have been hacked!"));
}); // Replaces all value elements with a string value saying "You have been hacked!"
```

### DataType and MapOptions

A `DataType` defines a type of data that can be read from an element.
The `MapOptions` class uses those data types to enforce option types on a map.

```java
MapOptions options = MapOptions.builder()
    .add("number_option", DataType.number())
    .add("string_options", DataType.string(), "default")
    .optional("optional_enum", DataType.enumeration(PizzaTopping.CHEESE))
    .build();
// Apply these options to a map
DataMap result = options.apply(someMap);
```
These operations will throw an exception if any values are invalid. 
Make sure to catch them, and properly report them to the user.

## The end

Thanks for having interest in my creations!

You can find me on Discord under the tag `thegates`.
