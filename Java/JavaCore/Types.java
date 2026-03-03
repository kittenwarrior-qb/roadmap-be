package Java.JavaCore;

public class Types {
    public static void main(String arg[]) {
        // Integer types
        byte myByte = 127;
        short myShort = 32000;
        int myInt = 2147483647;
        long myLong = 9223372036854775807L;  // Thêm 'L' ở cuối

        // Floating-point types
        float myFloat = 3.14f;    // Thêm 'f' ở cuối
        double myDouble = 3.14159265359;

        // Character
        char myChar = 'A';

        // Boolean
        boolean myBoolean = true;

        // In ra
        System.out.println("byte: " + myByte);
        System.out.println("short: " + myShort);
        System.out.println("int: " + myInt);
        System.out.println("long: " + myLong);
        System.out.println("float: " + myFloat);
        System.out.println("double: " + myDouble);
        System.out.println("char: " + myChar);
        System.out.println("boolean: " + myBoolean);
    }
}
