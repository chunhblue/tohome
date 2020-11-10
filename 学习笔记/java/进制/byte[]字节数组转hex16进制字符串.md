### byte[]字节数组转hex16进制字符串的三种方法

### 方法1

这种方法代码量是最少的，推荐（如果换成8进制，将16改成8就行了）

```java
private String bytesToHex(byte[] bytes) {    
    String hex = new BigInteger(1, bytes).toString(16);
}
```

### 方法2

```java
private String bytesToHex(byte[] bytes) {  
    StringBuilder sb = new StringBuilder();  
    for (byte b : bytes) {       
        sb.append(String.format("%02x", b));   
    }   
    return sb.toString();
}
```

### 方法3

```java
public String bytesToHex(byte[] bytes) {   
    char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'a','b','c','d','e','f'};     
    // 一个字节对应两个16进制数，所以长度为字节数组乘2  
    char[] resultCharArray = new char[bytes.length * 2];
    int index = 0;    
    for (byte b : bytes) {       
        resultCharArray[index++] = hexDigits[b>>>4 & 0xf]; 
        resultCharArray[index++] = hexDigits[b & 0xf];    
    }     
    return new String(resultCharArray); 
}
```