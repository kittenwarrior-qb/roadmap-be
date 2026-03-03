/**
 * VÍ DỤ 2: MULTIPLE COMPOSITION
 * Mối quan hệ: Computer HAS-A Processor, Memory, Storage
 * Thực hành: Tạo computer với nhiều components
 */

class Processor {
    private String brand;
    private String model;
    private int cores;
    private double ghz;
    
    public Processor(String brand, String model, int cores, double ghz) {
        this.brand = brand;
        this.model = model;
        this.cores = cores;
        this.ghz = ghz;
    }
    
    public void displayInfo() {
        System.out.println("CPU: " + brand + " " + model);
        System.out.println("  - Cores: " + cores);
        System.out.println("  - Speed: " + ghz + " GHz");
    }
    
    public int getCores() {
        return cores;
    }
}

class Memory {
    private int sizeGB;
    private String type;
    private int speedMHz;
    
    public Memory(int sizeGB, String type, int speedMHz) {
        this.sizeGB = sizeGB;
        this.type = type;
        this.speedMHz = speedMHz;
    }
    
    public void displayInfo() {
        System.out.println("RAM: " + sizeGB + "GB " + type);
        System.out.println("  - Speed: " + speedMHz + " MHz");
    }
    
    public int getSizeGB() {
        return sizeGB;
    }
}

class Storage {
    private int capacityGB;
    private String type;  // SSD or HDD
    
    public Storage(int capacityGB, String type) {
        this.capacityGB = capacityGB;
        this.type = type;
    }
    
    public void displayInfo() {
        System.out.println("Storage: " + capacityGB + "GB " + type);
    }
}

class Computer {
    private String brand;
    private Processor processor;  // Computer HAS-A Processor
    private Memory memory;        // Computer HAS-A Memory
    private Storage storage;      // Computer HAS-A Storage
    
    public Computer(String brand) {
        this.brand = brand;
    }
    
    public void setProcessor(Processor processor) {
        this.processor = processor;
    }
    
    public void setMemory(Memory memory) {
        this.memory = memory;
    }
    
    public void setStorage(Storage storage) {
        this.storage = storage;
    }
    
    public void displaySpecs() {
        System.out.println("\n╔════════════════════════════════╗");
        System.out.println("  " + brand + " Computer Specs");
        System.out.println("╚════════════════════════════════╝");
        
        if (processor != null) processor.displayInfo();
        if (memory != null) memory.displayInfo();
        if (storage != null) storage.displayInfo();
    }
    
    // Tính điểm hiệu năng dựa trên components
    public int calculatePerformanceScore() {
        int score = 0;
        if (processor != null) score += processor.getCores() * 10;
        if (memory != null) score += memory.getSizeGB() * 2;
        return score;
    }
}

public class Example2_MultipleComposition {
    public static void main(String[] args) {
        // TODO: Thực hành của bạn
        // 1. Tạo Gaming Computer với:
        //    - Brand: "Custom Gaming PC"
        //    - Processor: Intel i9-13900K, 24 cores, 5.8 GHz
        //    - Memory: 32GB DDR5, 6000 MHz
        //    - Storage: 2000GB SSD
        
        
        
        
        
        // 2. Hiển thị specs
        
        
        // 3. Tạo Office Computer với:
        //    - Brand: "Dell OptiPlex"
        //    - Processor: Intel i5-12400, 6 cores, 4.4 GHz
        //    - Memory: 16GB DDR4, 3200 MHz
        //    - Storage: 512GB SSD
        
        
        
        
        
        // 4. Hiển thị specs và so sánh performance score
        
        
        
        System.out.println("\n--- SOLUTION ---");
        solutionExample();
    }
    
    private static void solutionExample() {
        // Gaming PC
        Computer gamingPC = new Computer("Custom Gaming PC");
        gamingPC.setProcessor(new Processor("Intel", "i9-13900K", 24, 5.8));
        gamingPC.setMemory(new Memory(32, "DDR5", 6000));
        gamingPC.setStorage(new Storage(2000, "SSD"));
        gamingPC.displaySpecs();
        System.out.println("Performance Score: " + gamingPC.calculatePerformanceScore());
        
        // Office PC
        Computer officePC = new Computer("Dell OptiPlex");
        officePC.setProcessor(new Processor("Intel", "i5-12400", 6, 4.4));
        officePC.setMemory(new Memory(16, "DDR4", 3200));
        officePC.setStorage(new Storage(512, "SSD"));
        officePC.displaySpecs();
        System.out.println("Performance Score: " + officePC.calculatePerformanceScore());
    }
}
