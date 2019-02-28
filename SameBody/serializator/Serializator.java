package serializator;

import javafx.util.Pair;
import org.javatuples.Triplet;
import packets.ISerializable;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

public class Serializator {
    public static <T extends ISerializable> String serialize(T packet) throws SerializationException {
        StringBuilder result = new StringBuilder();
        Class cls = packet.getClass();
        String clsName = cls.getName();
        result.append("(").append(clsName.length()).append(")").append(clsName);
        Field[] fields = packet.getClass().getDeclaredFields();
        for (Field field:fields){
            String type = field.getType().getSimpleName();
            String str = "";
            String name = field.getName();
            String shortType = "";
            try {
                switch (type) {
                    case "int":
                        shortType = "in";
                        str = String.valueOf(field.getInt(packet));
                        break;
                    case "Int":
                        shortType = "In";
                        str = String.valueOf(field.getInt(packet));
                        break;
                    case "long":
                        shortType = "lo";
                        str = String.valueOf(field.getLong(packet));
                        break;
                    case "Long":
                        shortType = "Lo";
                        str = String.valueOf(field.getLong(packet));
                        break;
                    case "boolean":
                        shortType = "bo";
                        str = String.valueOf(field.getBoolean(packet));
                        break;
                    case "Boolean":
                        shortType = "Bo";
                        str = String.valueOf(field.getBoolean(packet));
                        break;
                    case "byte[]":
                        shortType = "[B";
                        str = String.valueOf(Arrays.toString((byte[]) field.get(packet)));
                        break;
                    case "String[]":
                        shortType = "[L";
                        str = String.valueOf(Arrays.toString((String[]) field.get(packet)));
                        break;
                    case "byte":
                        shortType = "by";
                        str = String.valueOf(field.getByte(packet));
                        break;
                    case "Byte":
                        shortType = "By";
                        str = String.valueOf(field.getByte(packet));
                        break;
                    case "double":
                        shortType = "do";
                        str = String.valueOf(field.getDouble(packet));
                        break;
                    case "Double":
                        shortType = "Do";
                        str = String.valueOf(field.getDouble(packet));
                        break;
                    case "String":
                        shortType = "St";
                        str = (String) field.get(packet);
                        break;
                    case "char":
                        shortType = "ch";
                        str = String.valueOf(field.getChar(packet));
                        break;
                }
                result.append(shortType);
                result.append("(").append(name.length()).append(")").append(name);
                result.append("(").append(str.length()).append(")").append(str);
            } catch (IllegalAccessException e){
                throw new SerializationException(e.getMessage(), e.getCause());
            }
        }
        return result.toString();
    }

    private static Pair<Integer, Integer> getLength(String[] data, int i) throws SerializationException {
        if (data[i].equals("(")){
            i++;
            StringBuilder length = new StringBuilder();
            while (!data[i].equals(")")){
                length.append(data[i]);
                i++;
            }
            i++;
            return new Pair<>(Integer.valueOf(length.toString()), i);
        }
        else throw new SerializationException();
    }

    private static Triplet<String, String, Integer> getVariable(String[] data, int i)
            throws SerializationException {
        Pair<Integer, Integer> res = getLength(data, i);
        int len = res.getKey();
        i = res.getValue();

        StringBuilder varName = new StringBuilder();
        for (int j = 0; j < len; j++, i++) {

            varName.append(data[i]);
        }

        res = getLength(data, i);
        len = res.getKey();
        i = res.getValue();

        StringBuilder varValue = new StringBuilder();
        for (int j = 0; j < len; j++, i++) {
            varValue.append(data[i]);
        }

        return new Triplet<>(varName.toString(), varValue.toString(), i);
    }

    private static byte[] parseByteArrayFromString(String strVariable){
        String[] temp = strVariable.substring(1, strVariable.length() - 1)
                .split(", ");
        byte[] res = new byte[temp.length];
        for (int j = 0; j < temp.length; j++)
            res[j] = Byte.parseByte(temp[j]);
        return res;
    }

    private static String[] parseStringArrayFromString(String strVariable){
        return strVariable.substring(1, strVariable.length() - 1)
                .split(", ");
    }

    public static <T extends ISerializable> T deserialize(byte[] raw) throws SerializationException {
        String[] data;
        try {
            data = new String(raw, "UTF-8").split("");
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException(e.getMessage(), e.getCause());
        }
        Pair<Integer, Integer> length = getLength(data, 0);
        int len = length.getKey();
        int index = length.getValue();
        StringBuilder className = new StringBuilder();
        while (len > 0){
            className.append(data[index]);
            index++;
            len -= 1;
        }

        Class<T> cls;
        try {
            cls = (Class<T>) Class.forName(className.toString());
        } catch (ClassNotFoundException e) {
            throw new SerializationException(e.getMessage(), e.getCause());
        }

        Constructor[] constrs = cls.getDeclaredConstructors();
        T clsInstance = null;
        for (Constructor constr:constrs){
            try{
                clsInstance = (T) constr.newInstance();
                break;
            }
            catch(Exception ignored){
            }
        }

        if (clsInstance == null) throw new SerializationException();
        Field[] fields = clsInstance.getClass().getDeclaredFields();

        for (int i = index; i < data.length-1;) {
            String type = data[i] + data[i+1];
            i += 2;

            Triplet<String, String, Integer> var = getVariable(data, i);
            String varName = var.getValue0();
            String varValue = var.getValue1();
            i = var.getValue2();

            for (Field f: fields)
                if (f.getName().equals(varName)) {
                    try {
                        f.setAccessible(true);
                        switch (type) {
                            case "St":
                                f.set(clsInstance, varValue);
                                break;
                            case "in":
                                f.setInt(clsInstance, Integer.valueOf(varValue));
                                break;
                            case "In":
                                f.setInt(clsInstance, Integer.valueOf(varValue));
                                break;
                            case "lo":
                                f.setLong(clsInstance, Integer.valueOf(varValue));
                                break;
                            case "Lo":
                                f.setLong(clsInstance, Integer.valueOf(varValue));
                                break;
                            case "bo":
                                f.setBoolean(clsInstance, Boolean.valueOf(varValue));
                                break;
                            case "Bo":
                                f.setBoolean(clsInstance, Boolean.valueOf(varValue));
                                break;
                            case "ch":
                                f.setChar(clsInstance, varValue.charAt(0));
                                break;
                            case "[B":
                                byte[] result = parseByteArrayFromString(varValue);
                                f.set(clsInstance, result);
                                break;
                            case "[L":
                                String[] res = parseStringArrayFromString(varValue);
                                f.set(clsInstance, res);
                                break;
                        }
                    } catch (IllegalAccessException e) {
                        throw new SerializationException(e.getMessage(), e.getCause());
                    }
                }
        }
        return clsInstance;
    }
}