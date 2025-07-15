# paper-client-kt

The Kotlin [PaperCache](https://papercache.io) client. The client supports all commands described in the wire protocol on the homepage.

## Example
```kotlin
import io.papercache.PaperClient;

var client = PaperClient("paper://127.0.0.1:3145");

client.set("hello", "world");
val got = client.get("hello");
```
