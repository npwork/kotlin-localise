# Simple Kotlin https://localise.biz/ API client
----
All Translations are in Properties format and **load once**

## What does it look like? (Code snippets)
#### Response from API
```json
{
    "en": {
        "hello-world": "Hello",
        "world": "World",
        "my": {
            "another": "Another"
        }
    }
}
```
#### "en" in Map
```properties
hello-world = Hello
world = World
my.another = Another
```

## How to use? (Code snippets)
```kotlin
val client = LocoClient("<API_KEY>")
val i18n = client.translations("en")
println(i18n.t("my.another"))
```
 

## How to add?
Add https://jitpack.io/ repository

#### Maven
```xml
<dependency>
    <groupId>com.github.npwork</groupId>
    <artifactId>kotlin-localise</artifactId>
    <version>-SNAPSHOT</version>
</dependency>
```

#### Gradle
```groovy
implementation 'com.github.npwork:kotlin-localise:-SNAPSHOT'
```
