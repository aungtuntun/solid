Solid
=====

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Solid-green.svg?style=flat)](https://android-arsenal.com/details/1/1955)

Solid is an Android library for data handling.

It provides:

* Lightweight and composable **data streams** with `Optional` implementation.

* Primitive array / wrapped array **converters**.

* `SolidList`, `SolidMap`, `SolidSet` - immutable, parcelable collections.

*Note: `SolidMap` is not `java.util.Collection` due to Android Parcelable Map issue.*

### Philosophy

*Solid* library adheres to the philosophy: "transform it as a stream, keep it as immutable".
Thus allowing to pass collections around without a fear that they can be changed by another part
of the application, while keeping the ability to transform data in a convenient way.

### Include

``` groovy
dependencies {
    def solidVersion = '2.0.0-alpha2'
    compile "info.android15.solid:streams:$solidVersion"
    compile "info.android15.solid:collections:$solidVersion"
}
```

# Lightweight data streams

*Solid* streams are similar to Java 8 streams but they are released under the MIT license and are absolutely free.

The usual approach on Android is to use RxJava for functional data handling, but it is too verbose and too heavy for
a non-event based usage pattern. My tests say that *Solid* streams are about 5 times faster than RxJava and take about
5 times less memory the same time on small data sets.

These streams are passive and do not emit items, they are not thread-safe, so they are much simpler.
If you're stuck with studying *RxJava* you may try to understand these streams first.

All Solid streams are sequential.

### How to use

All examples here with Java 8 syntax on. I recommend using [Gradle Retrolambda Plugin](https://github.com/evant/gradle-retrolambda)
to make your code shorter and to bring the functional power to your code.

You can take any `Iterable` or an array and turn it into a set of chained methods:

``` java
stream(asList(1, 3, 2))                // Iterable<Integer>
    .filter(it -> it < 3)              // only 1 and 2 items are not filtered
    .map(it -> Integer.toString(it))   // convert Integer values to String values
    .toList();
```

This code will result in a `List<String>` which contains `"1"` and `"2"` values.

Another example: we need to sort some items by name and then return their ids in a `SolidList`.

``` java
stream(namedEntities)
    .sort((left, right) -> left.name.compareTo(right.name))
    .map(it -> it.id)
    .collect(toSolidList());
```

Easy, isn't it? I believe you already know about the power of streaming operators,
so here is a very lightweight and convenient implementation, especially for needs of an Android developer.

For a list of stream operators see:
[StreamDemo.java](https://github.com/konmik/solid/blob/master/retrolambdatest/src/test/java/solid/stream/StreamDemo.java)

# Data converters and primitive arrays

Here is how converters look like:

``` java
int[] values = of(1, 2, 3)  // Iterable<Integer> at this point
    .collect(toInts())      // int[]
```

Currently Solid supports conversion of these primitive types: `byte`, `double`, `float`, `int`, `long`.

You can write your own converters if you wish - just implement a converting function and pass it into `Stream.collect()`.

*Solid* converters are quite powerful:

To convert a primitive array into an iterable stream just call one method.
Call two methods to convert them into an immutable parcelable list.

``` java
SolidList<Byte> list = box(new byte[]{1, 2, 3})
    .collect(toSolidList());
```

Easy.

Want to join two primitive arrays?

``` java
byte[] joined = box(new byte[]{1, 2, 3})
    .merge(box(new byte[]{4, 5, 6}))
    .collect(toBytes());
```

Remove a value from a primitive array?

``` java
byte[] array_1_3 = box(new byte[]{1, 2, 3})
    .separate((byte) 2)
    .collect(toBytes());
```

And so on. The amount of flexibility that iterable streams and converters provide is hard to get at the
beginning but as long as you use them, more and more ideas come into mind.

The full list of possible converters is here: [converters](https://github.com/konmik/solid/tree/master/solid/src/main/java/solid/converters)

# Solid collections

If you're a big fan of immutable data structures like me then you also probably miss `Parcelable` interface
implementation in *Guava*'s `ImmutableList`.

If you're not a big fan of immutability then you should be.

I recommend reading this library description to get started with immutability: [AutoValue](https://github.com/google/auto/tree/master/value).
The library has a very good Android port with `Parcelable` implementation: [AutoParcel](https://github.com/frankiesardo/auto-parcel).

There is yet another library that makes a good combo with `SolidList` - [Icepick](https://github.com/frankiesardo/icepick).
*Solid* collections can be safely passed between activities, services, intents
and threads, and it can be automatically saved into an activity/fragment `Bundle` with just one annotation. Amazing.

### Details

*Solid* collections are just a decorators around `ArrayList`, `LinkedHashMap` and `LinkedHashSet`,
so I do not think that any docs are needed.
`UnsupportedOperationException` become thrown on each method that tries to modify a *solid* collection.

If you're familiar with Guava's immutable collections - there is a difference that is good to know. *Solid* collections do
not have a support for *Builder* pattern - use `ArrayList`, `Stream`, `Map` and `Set` to prepare them.
