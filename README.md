# jintertype
Java Interface Type Pattern Implementation

## Benefits
* Very lightweight
* Minimum boilerplate code required
* Getting data is as fast as by traditional type wrapping approach
* In-build validation

## How to use
### Add maven dependency to your project

```xml
<dependency>
  <groupId>com.github.hammelion</groupId>
  <artifactId>jintertype</artifactId>
  <version>1.0</version>
</dependency>
```

### Create your own types

```java
public class Username implements Type<String> {
}
```

```java
public class Age implements Type<Integer> {
}
```

### And use them in your code
#### Initialization

```java
final Username bobsName = Type.of("Bob", Username.class);
final Age bobsAge = Type.of(50, Age.class);
```

#### Reading state

```java
final String bobsNameAsString = bobsName.value();
final Integer bobsAgeAsInt = bobsAge.value();
```

#### Null and Optional

```java
final String bobsNameAsString = Type.of("Bob", Username.class).value(); // Bob
final String nullName = Type.of(null, Username.class).value(); // null
final Optional<String> optionalName = Type.ofOptional(null, Username.class).value(); // Optional.empty()
```

### There is also an inbuild soultion for validation

```java
public class Id implements Type<String> {
  @Override public boolean isValid() {
     return value() == null || value().length() < 4;
  }
}
```

```java
final Id bobsId = Type.of("user123", Id.class); //OK
final Id wrongId = Type.of("123", Id.class); //IllegalArgumentException
```

### How to create a data structure
#### Traditional way

```java
public class User {
  private final Id id;
  private final Username username;
  private final Optional<Age> age;
  
  /**
  * In case of unsuccessful validation a structure will not be allowed to be created
  */
  public User(String id, String username, Integer age) {
    this.id = Type.of(id, Id.class);
    this.username = Type.of(username, Username.class);
    this.age = Type.ofOptional(age, Age.class);
  }
  
  // getters
}
```

#### Using Structure class

```java
public class User extends Structure {
  private final Id id;
  private final Username username;
  private final Optional<Age> age;
  
  /**
  * In case of unsuccessful validation an error will be saved to a structure
  */
  public User(String id, String username, Integer age) {
    this.id = of(id, Id.class);
    this.username = of(username, Username.class);
    this.age = ofOptional(age, Age.class);
  }
  
  // getters
}
```

```java
final User bob = new User("123", "Bob", 50);
if (bob.hasErrors()){
  // you can get all validation errors with bob._getErrors()
}
```

## Efficiency

* Read operation has the same efficiency as a traditional approach
* Write operation is 1.3 times slower then a traditional approach, because a new class is created


