import java.io.*;
import java.util.*;

public class ClosestWater {

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("cauldron.in"));
            int randomInteger = Integer.parseInt(br.readLine().trim());
            String[] secondRow = br.readLine().trim().split(" ");
            int N = Integer.parseInt(secondRow[0]);
            int K = Integer.parseInt(secondRow[1]);
            int C = Integer.parseInt(secondRow[2]);
            String[] volumesStr = br.readLine().trim().split(" ");
            int[] volumes = new int[N];
            for (int i = 0; i < N; i++) {
                volumes[i] = Integer.parseInt(volumesStr[i]);
            }
            br.close();

            Result result = knapsack(N, K, C, volumes);
            System.out.println("Maximum value: " + result.maxValue);
            System.out.println("Jars used: " + result.jarsUsed);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("cauldron.out"))) {
                bw.write(String.valueOf(result.maxValue));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Result {
        int maxValue;
        List<Integer> jarsUsed;

        public Result(int maxValue, List<Integer> jarsUsed) {
            this.maxValue = maxValue;
            this.jarsUsed = jarsUsed;
        }
    }

    public static Result knapsack(int N, int K, int C, int[] volumes) {
        int[] dp = new int[K + 1];
        int[] count = new int[K + 1];
        List<Integer>[] jarsUsed = new List[K + 1];

        for (int i = 0; i <= K; i++) {
            jarsUsed[i] = new ArrayList<>();
        }

        for (int i = 0; i < N; i++) {
            for (int j = K; j >= volumes[i]; j--) {
                int newValue = dp[j - volumes[i]] + volumes[i] + C;
                if (newValue > dp[j] ||
                        (newValue == dp[j] && ((C > 0 && count[j - volumes[i]] + 1 < count[j])
                                || (C <= 0 && count[j - volumes[i]] + 1 > count[j])))) {
                    dp[j] = newValue;
                    count[j] = count[j - volumes[i]] + 1;
                    jarsUsed[j] = new ArrayList<>(jarsUsed[j - volumes[i]]);
                    jarsUsed[j].add(volumes[i]);
                }
            }
        }

        return new Result(K + C * count[K], jarsUsed[K]);
    }
}