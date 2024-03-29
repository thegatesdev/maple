*This is a **WIP version** of this document for the upcoming v5.0.0 release.*

[![maple-banner-plain](doc/maple-banner-plain.svg)](#)

[![License](https://img.shields.io/github/license/thegatesdev/maple?style=flat-square&labelColor=%230C090D&color=%23EB5600)](#)
[![CodeFactor Grade](https://img.shields.io/codefactor/grade/github/thegatesdev/maple?style=flat-square&labelColor=%230C090D&color=%23EB5600)](https://www.codefactor.io/repository/github/thegatesdev/maple)
[![Last commit)](https://img.shields.io/github/last-commit/thegatesdev/maple?style=flat-square&labelColor=%230C090D&color=%23EB5600)](https://github.com/thegatesdev/maple/commits/main/)
[![Latest version tag](https://img.shields.io/github/v/release/thegatesdev/maple?style=flat-square&labelColor=%230C090D&color=%23EB5600)](https://github.com/thegatesdev/maple/releases)
[![Total downloads](https://img.shields.io/github/downloads/thegatesdev/maple/total?style=flat-square&labelColor=%230C090D&color=%23EB5600)](https://github.com/thegatesdev/maple/releases)

A clean, type safe intermediary structure in Java

*Updated for version 5.0.0*

## About

**Maple** was originally intended as a replacement to the Spigot Configuration API,
which allows Minecraft plugins to read data from configuration files.
This is still my use-case for this library, but the goals have grown way past that.

My goal with this library is to represent JSON like configuration values
in a clean and understandable structure, with plenty of utilities for different scenario's,
while taking inspiration from (and possibly improving upon) existing libraries.

Feel free to open an issue or submit a pull request if you have any concerns or suggestions.

## Including in your project

**Maple** is public on GitHub, meaning you can easily get it from JitPack.

Head to https://jitpack.io/#thegatesdev/maple,
select a version and follow the instructions for your build system.
Now you are ready to dive in!

## Setting some rules

Here are some rules to keep in mind while using **Maple**:

-  **`null` is not invited to the party.**
This library does not allow `null` values passed in, and guarantees that no `null` will ever be returned.

- **Non-exported packages are unsafe.**
The `module-info.java` defines the packages that are exported.
If you are not using the Java module system, it is still not recommended
to use the classes in the packages that are not exported. 
These packages are considered outside the public facing API and can change without warning.

## Parsing elements

**Maple** does not have its own parser yet.

Parsing an element structure can be done by adapting output from other parser implementations.
Currently, an adapter exists to convert SnakeYaml and SnakeYaml-Engine output to Maple...

Parse some data using SnakeYaml...
```java
String document = "hello: 25";
Object yamlData = new Yaml().load(document);
```
... or SnakeYaml-Engine...
```java
String document = "hello: 25";
Load yamlLoad = new Load(LoadSettings.builder().setLabel("Test configuration").build());
Object yamlData = yamlLoad.loadFromString(document);
```
... and adapt to the **Maple** structure.
```java
Adapter snakeYamlAdapter = new SnakeYamlAdapter();
Element element = snakeYamlAdapter.adapt(yamlData);

element.isDict(); // true
element.asDict().get("hello").getInt(); // 25
```

## Creating elements

You can directly create elements of primitive types...
```java
Element intElement = Element.of(30);
Element boolElement = Element.of(true);
Element stringElement = Element.of("foo");
```
For constructing lists and dictionaries, use the corresponding builders...
```java
ListElement listElement = ListElement.builder()
        .add(Element.of(30))
        .add(Element.of(true))
        .add(Element.of("foo"))
        .build();

DictElement dictElement = DictElement.builder()
        .put("int", Element.of(30))
        .put("bool", Element.of(true))
        .put("string", Element.of("foo"))
        .build();
```
Merging dictionaries can also be done using builders...
```java
DictElement original;
DictElement other;
DictElement merged = original.toBuilder()
        .putAll(other)
        .build();
```

## Getting values from elements

Obtaining values has never looked nicer...
```java
DictElement dictElement; // assuming the dictionary from the above example

dictElement.get("int").isNumber(); // true
boolean boolValue = dictElement.get("bool").getBool(); // true
boolean invalidBoolValue = dictElement.get("string").getBool(); // throws ElementTypeException
Optional<Element> notPresent = dictElement.find("not_present"); // empty optional
```

## Layouts

Layouts define and enforce a ... *layout* on some element.
You could compare them to *schema's* in the JSON world, but directly in the code...
```java
DictLayout layout = Layout.dictionary()
                .required("name", ElementType.STRING)
                .required("age", ElementType.NUMBER)
                .required("address", Layout.dictionary()
                        .required("country", ElementType.STRING)
                        .required("city", ElementType.STRING)
                        .build())
                .optional("occupation", Element.of("none"), ElementType.STRING)
                .build();
DictElement parsedElement = layout.parse(/*Some element*/);
```

## Goals

**Maple** is definitely not finished. Here are some of the features I'd like to implement in the future:

- Better pathing and error message support using some element wrapper.
- Create my own JSON, YAML and maybe TOML parser, instead of relying on outside parsers.
- Serialize and deserialize Layouts so they can be defined in files.
- Serialize and deserialize dictionaries to and from Java objects (and to proxy objects).
- Saving values back to the configuration.

## The end

Thank you for reading, and have a nice day!

You can find me on Discord under the tag `thegatesdev`.
