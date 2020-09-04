```
java.io.EOFException: End of input at line 1 column 2 path $.

at com.google.gson.stream.JsonReader.nextNonWhitespace(JsonReader.java:1397)
	at com.google.gson.stream.JsonReader.doPeek(JsonReader.java:495)
	at com.google.gson.stream.JsonReader.hasNext(JsonReader.java:415)
	at com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$Adapter.read(ReflectiveTypeAdapterFactory.java:216)
	at com.google.gson.Gson.fromJson(Gson.java:932)
```

解决：

```html
<input type="hidden" id="searchJson" value='${searchJson!}'/>
```

将  ${searchJson!}  的 “ ” 修改为 ‘ ’