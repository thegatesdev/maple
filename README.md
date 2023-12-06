![maple-banner-plain](https://github.com/thegatesdev/maple/assets/69715898/16368197-a6e0-4edf-9df4-2576db370412)

A clean, type safe intermediary structure

*Updated for version 4.1.0*

## Contents

- [About](#about)
- [Usage](#usage)
  - [Including the library in your project](#including-in-your-project)
  - [Basics](#element-basics)
  - [Conversion](#conversion)
  - [Transformation](#transforming)
  - [DataTypes and MapOptions](#datatype-and-mapoptions)

## About

**Maple** was made as a replacement for the Spigot Configuration API,
as that API was quite limiting and ugly for my use cases.

My goal with this library is to make it easy to wrap configuration values
in a clean and understandable structure, providing plenty of utility methods
for each scenario.

Feel free to open an issue, or even a pull request if you have any concerns or suggestions.

## Usage

### Including in your project

Since Maple is public on GitHub, you can find it on JitPack.
Just head to https://jitpack.io/#thegatesdev/maple, select a version and follow the instructions for your build system.

### Element Basics

**Building elements**

All elements are immutable. Creating elements is done as such;

```java
// Values ...
DataValue<String> stringValue = DataValue.of("Hello world!");
// Maps ...
DataMap myMap = DataMap.builder()
        .add("message", stringValue)
        .build();
// And lists!
DataList myList = DataList.builder()
        .add(stringValue)
        .build();
```

You can also use builders with existing data, using the `addFrom` methods.

**Checking types**

There are multiple methods to check the type of element itself;

```java
DataElement myElement = DataMap.EMPTY;

myElement.isList(); // False
myElement.isMap(); // True

ElementType type = myElement.type(); // ElementType.MAP

myElement.ifMap(map -> print("myElement is a map!"), () -> print("myElement is not a map!"));
```

**Getting from elements**

All `get` methods will throw when an invalid request is made.

```java
DataMap myMap;

// Plain elements
DataElement otherElement = myMap.get("somekey");

DataMap otherMap = myMap.getMap("mapkey");
DataList otherList = myMap.getList("listkey", DataList.EMTPY); // With default

DataElement optionalElement = myMap.find("invalid_key"); // Returns 'null' if not found

// Values
String stringValue = myMap.get("message", String.class);
String stringValue = myMap.find("message", String.class, "no message"); // With default
int intValue = myMap.getInt("count", 0);

myMap.each(element -> print("Found an element"));
```

### Conversion

Maple was meant to be used with data from other sources, like json or yaml configuration files.

To convert plain Java objects to a Maple structure, for example, the output from snakeyaml, use the `Conversion` class.
```java
Conversion conversion = new DefaultConversion();

Map<?,?> output = //.. e.g. load from file
DataMap data = conversion.convertMap(fileOutput);
```

This will convert the entire structure to a Maple representation.
The `DefaultConversion` implementation should be adequate for most use cases.

### Transforming

Every Maple element supports two transformation methods; `transform` and `crawl`. 
These methods have only one difference: 
`transform` visits only the *children* of an element,
while `crawl` visits *all descendants* of an element

A crawl operation to replace every value element with a message could look like this:

```java
DataMap myMap;
DataMap result = myMap.crawl(element -> {
    if (element.isValue()) return DataValue.of("You have been hacked!");
    return element;
});
```

### DataType and MapOptions

A `DataType` defines a type of data that can be read from an element.
The `MapOptions` class uses those data types to enforce and apply option types on a map.

```java
MapOptions options = MapOptions.builder()
    .add("number_option", DataType.number())
    .add("string_options", DataType.string(), "default")
    .optional("optional_enum", DataType.enumeration(PizzaTopping.class), PizzaTopping.CHEESE)
    .build();
// Apply these options to a map
DataMap result = options.apply(someMap);
```
These operations will throw an exception if any values are invalid. 
Make sure to catch them, and properly report them to the user.

## The end

Thanks for having interest in my creations!

You can find me on Discord under the tag `thegates`.
