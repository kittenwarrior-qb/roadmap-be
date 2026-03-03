/**
 * VÍ DỤ 3: CAR AND ENGINE - REAL WORLD EXAMPLE
 * Mối quan hệ: Car HAS-A Engine, Transmission
 * Thực hành: Tạo xe với engine khác nhau, start/stop
 */

class Engine {
    private String type;  // Electric, Gasoline, Diesel, Hybrid
    private int horsepower;
    private boolean isRunning;
    
    public Engine(String type, int horsepower) {
        this.type = type;
        this.horsepower = horsepower;
        this.isRunning = false;
    }
    
    public void start() {
        if (!isRunning) {
            isRunning = true;
            System.out.println("🔥 " + type + " engine started (" + horsepower + " HP)");
        } else {
            System.out.println("⚠️  Engine is already running");
        }
    }
    
    public void stop() {
        if (isRunning) {
            isRunning = false;
            System.out.println("🛑 Engine stopped");
        } else {
            System.out.println("⚠️  Engine is already off");
        }
    }
    
    public void accelerate() {
        if (isRunning) {
            System.out.println("🚀 Accelerating with " + horsepower + " HP!");
        } else {
            System.out.println("❌ Cannot accelerate - engine is off");
        }
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    
    public String getType() {
        return type;
    }
}

class Transmission {
    private String type;  // Manual, Automatic, CVT
    private int currentGear;
    
    public Transmission(String type) {
        this.type = type;
        this.currentGear = 0;  // Neutral
    }
    
    public void shiftUp() {
        if (currentGear < 6) {
            currentGear++;
            System.out.println("⚙️  Shifted to gear " + currentGear);
        }
    }
    
    public void shiftDown() {
        if (currentGear > 0) {
            currentGear--;
            System.out.println("⚙️  Shifted to gear " + currentGear);
        }
    }
    
    public void displayInfo() {
        System.out.println("Transmission: " + type + " (Gear: " + currentGear + ")");
    }
}

class Car {
    private String brand;
    private String model;
    private Engine engine;           // Car HAS-A Engine
    private Transmission transmission; // Car HAS-A Transmission
    
    public Car(String brand, String model) {
        this.brand = brand;
        this.model = model;
    }
    
    public void installEngine(Engine engine) {
        this.engine = engine;
        System.out.println("✅ " + engine.getType() + " engine installed in " + brand + " " + model);
    }
    
    public void installTransmission(Transmission transmission) {
        this.transmission = transmission;
        System.out.println("✅ Transmission installed");
    }
    
    public void start() {
        System.out.println("\n🚗 Starting " + brand + " " + model + "...");
        if (engine != null) {
            engine.start();
        } else {
            System.out.println("❌ No engine installed!");
        }
    }
    
    public void stop() {
        System.out.println("\n🚗 Stopping " + brand + " " + model + "...");
        if (engine != null) {
            engine.stop();
        }
    }
    
    public void drive() {
        System.out.println("\n🚗 Driving " + brand + " " + model + "...");
        if (engine != null && engine.isRunning()) {
            engine.accelerate();
            if (transmission != null) {
                transmission.shiftUp();
                transmission.shiftUp();
            }
        } else {
            System.out.println("❌ Start the engine first!");
        }
    }
    
    public void displayInfo() {
        System.out.println("\n╔════════════════════════════════╗");
        System.out.println("  " + brand + " " + model);
        System.out.println("╚════════════════════════════════╝");
        if (engine != null) {
            System.out.println("Engine: " + engine.getType());
            System.out.println("Status: " + (engine.isRunning() ? "Running ✓" : "Off"));
        }
        if (transmission != null) {
            transmission.displayInfo();
        }
    }
}

public class Example3_CarEngine {
    public static void main(String[] args) {
        // TODO: Thực hành của bạn
        // 1. Tạo Tesla Model 3 với Electric engine (283 HP) và Automatic transmission
        
        
        
        
        // 2. Hiển thị thông tin xe
        
        
        // 3. Thử drive mà chưa start (sẽ lỗi)
        
        
        // 4. Start xe và drive
        
        
        
        // 5. Stop xe
        
        
        // 6. Tạo Ford Mustang với Gasoline engine (450 HP) và Manual transmission
        
        
        
        
        // 7. Start, drive, và stop Mustang
        
        
        
        
        System.out.println("\n\n--- SOLUTION ---");
        solutionExample();
    }
    
    private static void solutionExample() {
        // Tesla
        Car tesla = new Car("Tesla", "Model 3");
        tesla.installEngine(new Engine("Electric", 283));
        tesla.installTransmission(new Transmission("Automatic"));
        tesla.displayInfo();
        
        tesla.drive();  // Sẽ lỗi vì chưa start
        tesla.start();
        tesla.drive();
        tesla.stop();
        
        // Ford Mustang
        Car mustang = new Car("Ford", "Mustang GT");
        mustang.installEngine(new Engine("Gasoline", 450));
        mustang.installTransmission(new Transmission("Manual"));
        mustang.displayInfo();
        
        mustang.start();
        mustang.drive();
        mustang.stop();
    }
}
