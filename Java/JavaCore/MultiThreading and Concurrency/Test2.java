// Ví dụ về Race Condition trong Java
public class Test2 {
    
    // Ví dụ 1: Counter không đồng bộ
    static class UnsafeCounter {
        private int count = 0;
        
        public void increment() {
            count++; // Race condition: read-modify-write không atomic
        }
        
        public int getCount() {
            return count;
        }
    }
    
    // Ví dụ 2: Bank Account với race condition
    static class BankAccount {
        private double balance = 1000;
        
        public void withdraw(double amount) {
            if (balance >= amount) { // Race condition ở đây
                try {
                    Thread.sleep(10); // Giả lập xử lý
                } catch (InterruptedException e) {}
                balance -= amount;
                System.out.println(Thread.currentThread().getName() + 
                    " rút " + amount + ", còn lại: " + balance);
            }
        }
        
        public double getBalance() {
            return balance;
        }
    }
    
    // Ví dụ 3: Shared Resource
    static class SharedResource {
        private int value = 0;
        
        public void updateValue() {
            int temp = value;
            temp = temp + 1;
            // Race condition: thread khác có thể thay đổi value ở đây
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {}
            value = temp;
        }
        
        public int getValue() {
            return value;
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Demo Race Conditions ===\n");
        
        // Test 1: Counter Race Condition
        testUnsafeCounter();
        
        // Test 2: Bank Account Race Condition
        testBankAccount();
        
        // Test 3: Shared Resource Race Condition
        testSharedResource();
    }
    
    private static void testUnsafeCounter() throws InterruptedException {
        System.out.println("1. UNSAFE COUNTER:");
        UnsafeCounter counter = new UnsafeCounter();
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        System.out.println("Kết quả mong đợi: 2000");
        System.out.println("Kết quả thực tế: " + counter.getCount());
        System.out.println("Race condition xảy ra!\n");
    }
    
    private static void testBankAccount() throws InterruptedException {
        System.out.println("2. BANK ACCOUNT:");
        BankAccount account = new BankAccount();
        System.out.println("Số dư ban đầu: " + account.getBalance());
        
        Thread t1 = new Thread(() -> account.withdraw(600), "Thread-1");
        Thread t2 = new Thread(() -> account.withdraw(600), "Thread-2");
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        System.out.println("Số dư cuối: " + account.getBalance());
        System.out.println("Có thể bị âm do race condition!\n");
    }
    
    private static void testSharedResource() throws InterruptedException {
        System.out.println("3. SHARED RESOURCE:");
        SharedResource resource = new SharedResource();
        
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> resource.updateValue());
            threads[i].start();
        }
        
        for (Thread t : threads) {
            t.join();
        }
        
        System.out.println("Kết quả mong đợi: 10");
        System.out.println("Kết quả thực tế: " + resource.getValue());
        System.out.println("Race condition làm mất dữ liệu!");
    }
}
