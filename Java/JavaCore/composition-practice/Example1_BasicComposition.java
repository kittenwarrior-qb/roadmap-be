/**
 * VÍ DỤ 1: BASIC COMPOSITION
 * Mối quan hệ: Person HAS-A Job
 * Thực hành: Tạo Person với Job, thay đổi job, hiển thị thông tin
 */

class Job {
    private String role;
    private long salary;
    private String company;
    
    public Job(String role, long salary, String company) {
        this.role = role;
        this.salary = salary;
        this.company = company;
    }
    
    public String getRole() {
        return role;
    }
    
    public long getSalary() {
        return salary;
    }
    
    public String getCompany() {
        return company;
    }
    
    public void displayJobInfo() {
        System.out.println("Role: " + role);
        System.out.println("Salary: $" + salary);
        System.out.println("Company: " + company);
    }
}

class Person {
    private String name;
    private int age;
    private Job job;  // Composition: Person HAS-A Job
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    // Setter để gán job
    public void setJob(Job job) {
        this.job = job;
    }
    
    public void displayInfo() {
        System.out.println("\n=== PERSON INFO ===");
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        
        if (job != null) {
            System.out.println("\nJob Details:");
            job.displayJobInfo();
        } else {
            System.out.println("Status: Unemployed");
        }
    }
    
    // Delegate method
    public long getSalary() {
        return job != null ? job.getSalary() : 0;
    }
}

public class Example1_BasicComposition {
    public static void main(String[] args) {
        Person alice = new Person("Alice", 25);

        Job job1 = new Job("Software Engineer", 80000, "Google");
        alice.setJob(job1);
        alice.displayInfo();
        
        System.out.println("\nAlice's current salary: $" + alice.getSalary());
    }
    
}
