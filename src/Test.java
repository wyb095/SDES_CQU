import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class Test {
    public static void main(String[] args) throws Exception {
        System.out.println("S-DES算法");
        
        Scanner scanner = new Scanner(System.in);

        // 输入明文数量
        System.out.println("请输入明密文数量：");
        int n = scanner.nextInt();
        scanner.nextLine(); // 读取多余的换行符

        String[] ming = new String[n];
        String[] mi = new String[n];

        // 输入明文和密文
        for (int i = 0; i < n; i++) {
            System.out.println("请输入第 " + (i + 1) + " 组明文：");
            ming[i] = scanner.nextLine();

            System.out.println("请输入第 " + (i + 1) + " 组密文：");
            mi[i] = scanner.nextLine();
        }

        long t1 = System.currentTimeMillis();
        List<String> k = BruteForceConcurrent(ming, mi);
        long t2 = System.currentTimeMillis();
        System.out.println("暴力破解得到的密钥：" + k);
        System.out.println("破解时间：" + (t2 - t1) + "ms");
        
        GUI ui = new GUI();
        ui.setBounds(100, 100, 1000, 500);
    }

    public static String ToBinary(int num,int digit) {
        String binStr = "";
        for (int i = digit-1; i >= 0; i--) {
            binStr += (num >> i) & 1;
        }
        return binStr;
    }

    public static List<String> BruteForceConcurrent(String[] ming, String[] mi) throws InterruptedException, ExecutionException {
        List<String> keys = new ArrayList<>();
        int numThreads = Runtime.getRuntime().availableProcessors(); // 获取可用的核心数量
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        List<Future<List<String>>> futures = new ArrayList<>();

        for (int k = -512; k < 512; k++) {
            final int key = k;
            futures.add(executor.submit(() -> {
                List<String> validKeys = new ArrayList<>();
                String k1 = ToBinary(key,10);
                SDES des = new SDES();
                for (int i = 0; i < ming.length; i++) {
                    String miwen = des.encrypt(ming[i], k1);
                    if (!miwen.equals(mi[i]))
                        break;
                    else if (miwen.equals(mi[i]) && i == ming.length - 1)
                        validKeys.add(k1);
                }
                return validKeys;
            }));
        }

        for (Future<List<String>> future : futures) {
            keys.addAll(future.get());
        }

        executor.shutdown();

        return keys;
    }
}
