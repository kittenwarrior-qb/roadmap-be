import java.util.ArrayList;
import java.util.List;

/**
 * VÍ DỤ 5: CHALLENGE - UNIVERSITY SYSTEM
 * Tự thiết kế và implement hệ thống University với Composition
 * 
 * YÊU CẦU:
 * 1. University HAS-A List<Department>
 * 2. Department HAS-A List<Course>
 * 3. Course HAS-A Professor và List<Student>
 * 4. Student HAS-A List<Course> (enrolled courses)
 * 
 * CHỨC NĂNG CẦN IMPLEMENT:
 * - Add department to university
 * - Add course to department
 * - Enroll student to course
 * - Display university structure
 * - Calculate total students in university
 */

// TODO: Implement Professor class
class Professor {
    // Gợi ý: name, id, specialization
}

// TODO: Implement Student class
class Student {
    // Gợi ý: name, id, enrolledCourses (List<Course>)
}

// TODO: Implement Course class
class Course {
    // Gợi ý: courseName, courseCode, professor, students (List<Student>)
}

// TODO: Implement Department class
class Department {
    // Gợi ý: departmentName, courses (List<Course>)
}

// TODO: Implement University class
class University {
    // Gợi ý: universityName, departments (List<Department>)
}

public class Example5_Challenge {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("  CHALLENGE: UNIVERSITY SYSTEM");
        System.out.println("╚════════════════════════════════════════════╝");
        
        // TODO: Tạo hệ thống University
        // 1. Tạo University "MIT"
        
        
        // 2. Tạo Department "Computer Science"
        
        
        // 3. Tạo Professor "Dr. Smith" chuyên về "AI"
        
        
        // 4. Tạo Course "Machine Learning" (CS101) với Professor Smith
        
        
        // 5. Tạo 3 Students: Alice, Bob, Charlie
        
        
        
        
        // 6. Enroll students vào course
        
        
        
        
        // 7. Add course vào department
        
        
        // 8. Add department vào university
        
        
        // 9. Tạo thêm Department "Mathematics" với 1 course và 2 students
        
        
        
        
        
        
        // 10. Display toàn bộ university structure
        
        
        System.out.println("\n💡 Hint: Xem solution bên dưới nếu cần!");
        System.out.println("Uncomment dòng dưới để xem đáp án:");
        // solutionExample();
    }
    
    private static void solutionExample() {
        System.out.println("\n\n--- SOLUTION COMING SOON ---");
        System.out.println("Hãy thử tự implement trước!");
        System.out.println("\nCác bước gợi ý:");
        System.out.println("1. Implement từng class với constructor và methods cơ bản");
        System.out.println("2. Implement composition relationships (HAS-A)");
        System.out.println("3. Implement methods để add/remove/display");
        System.out.println("4. Test từng phần nhỏ trước khi ghép lại");
    }
}
