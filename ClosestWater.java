package board;

import java.io.*;
import java.util.*;

public class ClosestWater {
    public static void main(String[] args) throws IOException {
        // Create Scanner to read from file "cauldron.in"
        Scanner scanner = new Scanner(new File("cauldron.in"));
        // Create PrintWriter to write to file "cauldron.out" (board) and "cauldron2.out" (final result)
        PrintWriter boardOut = new PrintWriter(new File("cauldron.out"));
        PrintWriter resultOut = new PrintWriter(new File("cauldron2.out"));

        // Read the random number (not used in logic)
        int randomValue = scanner.nextInt();
        // Read N (number of bottles), K (target water amount), and C
        int N = scanner.nextInt();
        int K = scanner.nextInt();
        int C = scanner.nextInt();
        int[] bottles = new int[N];

        // Read bottle values from the same row
        for (int i = 0; i < N; i++) {
            bottles[i] = scanner.nextInt();
        }

        // Sort bottles in descending order
        Arrays.sort(bottles);
        reverseArray(bottles);

        // Generate board
        int[][] board = generateBoard(bottles, N, K);

        // Write the board to "cauldron.out"
        printBoard(board, bottles, N, K, boardOut);

        // Perform the process to calculate M and the final result
        int M = calculateM(board, bottles, N, K);

        // Calculate the final result: K + (M * C)
        int finalResult = K + (M * C);

        // Write the final result to "cauldron2.out"
        resultOut.println(finalResult);

        scanner.close();
        boardOut.close();
        resultOut.close();
    }

    private static int[][] generateBoard(int[] bottles, int N, int K) {
        int[][] board = new int[N][K + 1]; // Rows: bottles, Columns: 0 to K
        Set<Integer> globalSeen = new HashSet<>(); // Track numbers that have been processed globally

        // Fill the board
        for (int i = 0; i < N; i++) {
            int wi = bottles[i];
            // Mark the position K - wi if it's within bounds
            if (K - wi >= 0 && K - wi <= K) {
                board[i][K - wi] = 1;
                globalSeen.add(K - wi);
            }

            // Subtract wi from numbers that have a 1 in their column (excluding the current row)
            for (int j = 0; j <= K; j++) {
                if (globalSeen.contains(j) && j - wi >= 0) {
                    // Check if the 1 in column j is not from the current row
                    boolean isFromOtherRow = false;
                    for (int row = 0; row < N; row++) {
                        if (row != i && board[row][j] == 1) {
                            isFromOtherRow = true;
                            break;
                        }
                    }

                    // Mark the result with 1 if the 1 is from another row
                    if (isFromOtherRow) {
                        board[i][j - wi] = 1;
                        globalSeen.add(j - wi);
                    }
                }
            }
        }

        return board;
    }

    private static void printBoard(int[][] board, int[] bottles, int N, int K, PrintWriter out) {
        // Print the board
        // First row: blank space followed by numbers 0 to K
        out.print("   "); // Blank space for the first cell
        for (int j = 0; j <= K; j++) {
            out.print(j + " ");
        }
        out.println();

        // Subsequent rows: bottle value followed by board values
        for (int i = 0; i < N; i++) {
            out.print(bottles[i] + " "); // Bottle value
            for (int j = 0; j <= K; j++) {
                out.print(board[i][j] + " ");
            }
            out.println();
        }
    }

    private static int calculateM(int[][] board, int[] bottles, int N, int K) {
        int M = 0;
        int currentValue = -1; // Initialize to an invalid value

        // Find the initial value closest to zero (smallest j with a 1)
        for (int j = 0; j <= K; j++) {
            for (int i = 0; i < N; i++) {
                if (board[i][j] == 1) {
                    currentValue = j;
                    break;
                }
            }
            if (currentValue != -1) break; // Stop at the smallest j
        }

        if (currentValue == -1) return 0; // No valid starting point

        // Repeat the process until the result is larger than K
        while (true) {
            // Find the lowest row with a 1 under the current value
            int lowestRow = -1;
            for (int i = 0; i < N; i++) {
                if (board[i][currentValue] == 1) {
                    lowestRow = i;
                    break;
                }
            }

            if (lowestRow == -1) break; // No more 1s found

            // Add the wi of the lowest row to the current value
            int wi = bottles[lowestRow];
            currentValue += wi;
            M++;

            // Stop if the result is larger than or equal to K
            if (currentValue >= K) break;
        }

        return M;
    }

    private static void reverseArray(int[] arr) {
        int left = 0, right = arr.length - 1;
        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }
}