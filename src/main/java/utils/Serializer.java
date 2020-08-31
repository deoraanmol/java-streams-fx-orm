package utils;

import java.io.*;

public class Serializer {

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(byteArrayOutputStream);
        o.writeObject(obj);
        return byteArrayOutputStream.toByteArray();
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(byteArrayInputStream);
        return o.readObject();
    }
}
