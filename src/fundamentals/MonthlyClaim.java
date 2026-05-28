//Challenge 5 — Arrays Under Pressure

//Store:
//12 monthly insurance claims.

//Tasks:=> total claims, highest claim, lowest claim, average claim

//Use loops.
package fundamentals;

import java.util.Arrays;

public class MonthlyClaim {
     static void main() {
        int [] claims = {3, 5, 6, 7, 12, 40, 17, 87, 34, 2, 9, 12};
        int totalClaims = Arrays.stream(claims).sum();
        int lowestClaim = Arrays.stream(claims).min().getAsInt();
        int highestClaim = Arrays.stream(claims).max().getAsInt();
        double averageClaim = (double) totalClaims / claims.length;
        Arrays.sort(claims);

        System.out.println(Arrays.toString(claims));
        System.out.println(claims.length);
        System.out.println("total claims is  " + totalClaims);
        System.out.println("lowest claim is " + lowestClaim);
        System.out.println("highest claim is " + highestClaim);
        System.out.println("average claims is " + averageClaim);
    }
}