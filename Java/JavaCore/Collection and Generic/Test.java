import java.util.*;

public class Test {
    public static void main(String[] args) {
        // 1. Generic với Collection
        System.out.println("=== 1. Generic với Collection ===");
        List<String> names = new ArrayList<>();
        names.add("An");
        names.add("Bình");
        names.add("Cường");
        
        for (String name : names) {
            System.out.println(name);
        }
        
        // 2. Generic Class
        System.out.println("\n=== 2. Generic Class ===");
        Box<Integer> intBox = new Box<>(123);
        System.out.println("Integer Box: " + intBox.get());
        
        Box<String> strBox = new Box<>("Hello Generic");
        System.out.println("String Box: " + strBox.get());
        
        // 3. Generic với nhiều type parameters
        System.out.println("\n=== 3. Generic với nhiều type parameters ===");
        Pair<String, Integer> pair = new Pair<>("Tuổi", 25);
        System.out.println(pair.getKey() + ": " + pair.getValue());
        
        // 4. Generic Method
        System.out.println("\n=== 4. Generic Method ===");
        Integer[] intArray = {1, 2, 3, 4, 5};
        String[] strArray = {"Java", "Python", "C++"};
        
        printArray(intArray);
        printArray(strArray);
        
        // 5. Bounded Type Parameters
        System.out.println("\n=== 5. Bounded Type Parameters ===");
        System.out.println("Max của 3, 5, 7: " + findMax(3, 5, 7));
        System.out.println("Max của 'a', 'z', 'm': " + findMax('a', 'z', 'm'));
        
        // 6. Wildcard
        System.out.println("\n=== 6. Wildcard ===");
        List<Integer> intList = Arrays.asList(1, 2, 3);
        List<Double> doubleList = Arrays.asList(1.1, 2.2, 3.3);
        
        printList(intList);
        printList(doubleList);
        
        sumOfList(intList);
        sumOfList(doubleList);
    }
    
    // Generic Method
    public static <T> void printArray(T[] array) {
        for (T element : array) {
            System.out.print(element + " ");
        }
        System.out.println();
    }
    
    // Bounded Type Parameter - chỉ chấp nhận type implement Comparable
    public static <T extends Comparable<T>> T findMax(T x, T y, T z) {
        T max = x;
        if (y.compareTo(max) > 0) max = y;
        if (z.compareTo(max) > 0) max = z;
        return max;
    }
    
    // Unbounded Wildcard
    public static void printList(List<?> list) {
        for (Object obj : list) {
            System.out.print(obj + " ");
        }
        System.out.println();
    }
    
    // Upper Bounded Wildcard
    public static void sumOfList(List<? extends Number> list) {
        double sum = 0.0;
        for (Number num : list) {
            sum += num.doubleValue();
        }
        System.out.println("Tổng: " + sum);
    }
}

// Generic Class với 1 type parameter
class Box<T> {
    private T value;
    
    public Box(T value) {
        this.value = value;
    }
    
    public T get() {
        return value;
    }
    
    public void set(T value) {
        this.value = value;
    }
}

// Generic Class với nhiều type parameters
class Pair<K, V> {
    private K key;
    private V value;
    
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() {
        return key;
    }
    
    public V getValue() {
        return value;
    }
}
