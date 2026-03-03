static keyword in Java is used a lot in java programming. Java static keyword is used to create a Class level variable in java. static variables and methods are part of the class, not the instances of the class.

static keyword in java
java static, static keyword in javaJava static keyword can be used in five cases as shown in below image.static keyword in javaWe will discuss four of them here, the fifth one was introduced in Java 8 and that has been discussed at Java 8 interface changes.

Java static variable
We can use static keyword with a class level variable. A static variable is a class variable and doesn’t belong to Object/instance of the class. Since static variables are shared across all the instances of Object, they are not thread safe. Usually, static variables are used with the final keyword for common resources or constants that can be used by all the objects. If the static variable is not private, we can access it with ClassName.variableName

    //static variable example
    private static int count;
    public static String str;
    public static final String DB_USER = "myuser";
Java static method
Same as static variable, static method belong to class and not to class instances. A static method can access only static variables of class and invoke only static methods of the class. Usually, static methods are utility methods that we want to expose to be used by other classes without the need of creating an instance. For example Collections class. Java Wrapper classes and utility classes contains a lot of static methods. The main() method that is the entry point of a java program itself is a static method.

    //static method example
    public static void setCount(int count) {
        if(count > 0)
        StaticExample.count = count;
    }
    
    //static util method
    public static int addInts(int i, int...js){
        int sum=i;
        for(int x : js) sum+=x;
        return sum;
    }
From Java 8 onwards, we can have static methods in interfaces too. For more details please read Java 8 interface changes.

Java static block
Java static block is the group of statements that gets executed when the class is loaded into memory by Java ClassLoader. Static block is used to initialize the static variables of the class. Mostly it’s used to create static resources when the class is loaded. We can’t access non-static variables in the static block. We can have multiple static blocks in a class, although it doesn’t make much sense. Static block code is executed only once when the class is loaded into memory.

    static{
        //can be used to initialize resources when class is loaded
        System.out.println("StaticExample static block");
        //can access only static variables and methods
        str="Test";
        setCount(2);
    }
Java Static Class
We can use static keyword with nested classes. static keyword can’t be used with top-level classes. A static nested class is same as any other top-level class and is nested for only packaging convenience. Read: Java Nested Classes

Let’s see all the static keyword in java usage in a sample program. StaticExample.java

package com.journaldev.misc;

public class StaticExample {

    //static block
    static{
        //can be used to initialize resources when class is loaded
        System.out.println("StaticExample static block");
        //can access only static variables and methods
        str="Test";
        setCount(2);
    }
    
    //multiple static blocks in same class
    static{
        System.out.println("StaticExample static block2");
    }
    
    //static variable example
    private static int count; //kept private to control its value through setter
    public static String str;
    
    public int getCount() {
        return count;
    }

    //static method example
    public static void setCount(int count) {
        if(count > 0)
        StaticExample.count = count;
    }
    
    //static util method
    public static int addInts(int i, int...js){
        int sum=i;
        for(int x : js) sum+=x;
        return sum;
    }

    //static class example - used for packaging convenience only
    public static class MyStaticClass{
        public int count;
        
    }

}
Let’s see how to use static variable, method and static class in a test program. TestStatic.java

package com.journaldev.misc;

public class TestStatic {

    public static void main(String[] args) {
        StaticExample.setCount(5);
        
        //non-private static variables can be accessed with class name
        StaticExample.str = "abc";
        StaticExample se = new StaticExample();
        System.out.println(se.getCount());
        //class and instance static variables are same
        System.out.println(StaticExample.str +" is same as "+se.str);
        System.out.println(StaticExample.str == se.str);
        
        //static nested classes are like normal top-level classes
        StaticExample.MyStaticClass myStaticClass = new StaticExample.MyStaticClass();
        myStaticClass.count=10;
        
        StaticExample.MyStaticClass myStaticClass1 = new StaticExample.MyStaticClass();
        myStaticClass1.count=20;
        
        System.out.println(myStaticClass.count);
        System.out.println(myStaticClass1.count);
    }
    
}
The output of the above static keyword in java example program is:

StaticExample static block
StaticExample static block2
5
abc is same as abc
true
10
20
Notice that static block code is executed first and only once as soon as class is loaded into memory. Other outputs are self-explanatory.

Java static import
Normally we access static members using Class reference, from Java 1.5 we can use java static import to avoid class reference. Below is a simple example of Java static import.

package com.journaldev.test;

public class A {

	public static int MAX = 1000;
	
	public static void foo(){
		System.out.println("foo static method");
	}
}
package com.journaldev.test;

import static com.journaldev.test.A.MAX;
import static com.journaldev.test.A.foo;

public class B {

	public static void main(String args[]){
		System.out.println(MAX); //normally A.MAX
		foo(); // normally A.foo()
	}
}
Notice the import statements, for static import we have to use import static followed by the fully classified static member of a class. For importing all the static members of a class, we can use * as in import static com.journaldev.test.A.*;. We should use it only when we are using the static variable of a class multiple times, it’s not good for readability. Java break
There are two forms of break statement - unlabeled and labeled. Mostly break statement is used to terminate a loop based on some condition, for example break the processing if exit command is reached. Unlabeled break statement is used to terminate the loop containing it and can be used with switch, for, while and do-while loops.

break in java example
Here is an example showing java break statement usage in for loop, while loop and do-while loop.

package com.journaldev.util;

package com.journaldev.util;

public class JavaBreak {

	public static void main(String[] args) {
		String[] arr = { "A", "E", "I", "O", "U" };

		// find O in the array using for loop
		for (int len = 0; len < arr.length; len++) {
			if (arr[len].equals("O")) {
				System.out.println("Array contains 'O' at index: " + len);
				// break the loop as we found what we are looking for
				break;
			}
		}

		// use of break in while loop
		int len = 0;
		while (len < arr.length) {
			if (arr[len].equals("E")) {
				System.out.println("Array contains 'E' at index: " + len);
				// break the while loop as we found what we are looking for
				break;
			}
			len++;
		}

		len = 0;
		// use of break in do-while loop
		do {
			if (arr[len].equals("U")) {
				System.out.println("Array contains 'U' at index: " + len);
				// break the while loop as we found what we are looking for
				break;
			}
			len++;
		} while (len < arr.length);
	}

}
java break statement, break in javaNote that if we remove break statement, there won’t be any difference in the output of the program. For small iterations like in this example, there is not much of a performance benefit. But if the iterator size is huge, then it can save a lot of processing time.

Java break label
Labeled break statement is used to terminate the outer loop, the loop should be labeled for it to work. Here is an example showing java break label statement usage.

package com.journaldev.util;

public class JavaBreakLabel {

	public static void main(String[] args) {
		int[][] arr = { { 1, 2 }, { 3, 4 }, { 9, 10 }, { 11, 12 } };
		boolean found = false;
		int row = 0;
		int col = 0;
		// find index of first int greater than 10
		searchint:

		for (row = 0; row < arr.length; row++) {
			for (col = 0; col < arr[row].length; col++) {
				if (arr[row][col] > 10) {
					found = true;
					// using break label to terminate outer statements
					break searchint;
				}
			}
		}
		if (found)
			System.out.println("First int greater than 10 is found at index: [" + row + "," + col + "]");
	}

}
java break label
Java break
There are two forms of break statement - unlabeled and labeled. Mostly break statement is used to terminate a loop based on some condition, for example break the processing if exit command is reached. Unlabeled break statement is used to terminate the loop containing it and can be used with switch, for, while and do-while loops.

break in java example
Here is an example showing java break statement usage in for loop, while loop and do-while loop.

package com.journaldev.util;

package com.journaldev.util;

public class JavaBreak {

	public static void main(String[] args) {
		String[] arr = { "A", "E", "I", "O", "U" };

		// find O in the array using for loop
		for (int len = 0; len < arr.length; len++) {
			if (arr[len].equals("O")) {
				System.out.println("Array contains 'O' at index: " + len);
				// break the loop as we found what we are looking for
				break;
			}
		}

		// use of break in while loop
		int len = 0;
		while (len < arr.length) {
			if (arr[len].equals("E")) {
				System.out.println("Array contains 'E' at index: " + len);
				// break the while loop as we found what we are looking for
				break;
			}
			len++;
		}

		len = 0;
		// use of break in do-while loop
		do {
			if (arr[len].equals("U")) {
				System.out.println("Array contains 'U' at index: " + len);
				// break the while loop as we found what we are looking for
				break;
			}
			len++;
		} while (len < arr.length);
	}

}
java break statement, break in javaNote that if we remove break statement, there won’t be any difference in the output of the program. For small iterations like in this example, there is not much of a performance benefit. But if the iterator size is huge, then it can save a lot of processing time.

Java break label
Labeled break statement is used to terminate the outer loop, the loop should be labeled for it to work. Here is an example showing java break label statement usage.

package com.journaldev.util;

public class JavaBreakLabel {

	public static void main(String[] args) {
		int[][] arr = { { 1, 2 }, { 3, 4 }, { 9, 10 }, { 11, 12 } };
		boolean found = false;
		int row = 0;
		int col = 0;
		// find index of first int greater than 10
		searchint:

		for (row = 0; row < arr.length; row++) {
			for (col = 0; col < arr[row].length; col++) {
				if (arr[row][col] > 10) {
					found = true;
					// using break label to terminate outer statements
					break searchint;
				}
			}
		}
		if (found)
			System.out.println("First int greater than 10 is found at index: [" + row + "," + col + "]");
	}

}
java break label