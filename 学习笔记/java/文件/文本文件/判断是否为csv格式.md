 通过读取CSV文件头，判断文件是是否属于CSV文件类型，一般而言仅仅只是通过文件后缀来判断该文件所属的类型，这样是不合理的，只要更改一下文件后缀就无法识别这个文件到底是不是正确的文件格式，把可执行的文件后缀改为.CSV如果是通过判断文件后缀来识别文件类型，这样肯定是行不通的，因为exe的文件格式肯定不是CSV的格式，如果提前判断出这个文件头的这样就能定位这个文件是不是我们所需要的文件类型，避免对错误的文件进行解析。同样也可以在某种程度上保护服务器的安全。 



```java
    public static void main(String[] args) throws Exception {
		String src = "C:/dataTemp/Url使用.csv";
		FileInputStream is = new FileInputStream(src);
		System.out.println(judgeIsCSV(is));
	}

    /**
     * 判断是否为csv文件
     * @param is
     * @return
     */
    public static boolean judgeIsCSV(FileInputStream is){
        try {
            byte[] b = new byte[4];
            is.read(b, 0, b.length);
            return bytes2HexString(b).contains("5B75726C");//CSV文件的头部的前4个字节
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String bytes2HexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }
```

