# Proquint

[![CircleCI](https://circleci.com/gh/mmakowski/proquint.svg?style=svg)](https://circleci.com/gh/mmakowski/proquint)

A Java implementation of [A Proposal for Proquints: Identifiers that are Readable, Spellable, and Pronounceable](https://arxiv.org/html/0901.4016).

## Setup

### Maven

TODO

### Gradle

TODO

## Usage

Import the `Proquint` class:

```java
import com.mmakowski.proquint.Proquint;
```

Encode bytes to proquint string:

```java
byte[] bytes = new byte[] { 0x12, 0x34, 0x56, 0x78};
String encoded = Proquint.fromBytes(bytes);
```

Decode bytes from proquint string:

```java
String proquint = "damuh-jinum";
byte[] decoded = Proquint.toBytes(proquint);
```

## License

[Apache 2.0](https://opensource.org/licenses/Apache-2.0)
