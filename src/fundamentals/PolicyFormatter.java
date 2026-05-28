package fundamentals;

public class PolicyFormatter {
    public static void main(String[] args) {
        // Raw input with extra spaces and mixed case
        String rawPolicy = "  POLICY: AUTO-9921 | HOLDER: John Doe | COST: 1200  ";

        // 1. TRANSFORMATION: Clean and normalize
        // .trim() removes outer spaces
        // .toUpperCase() makes everything consistent for easier searching
        String cleanText = rawPolicy.trim().toUpperCase();
        System.out.println("1. Transformed: " + cleanText);

        // 2. EXTRACTION: Cut out specific parts
        // We know "POLICY: " is 8 characters long
        int startId = 8;
        int endId = cleanText.indexOf(" |");
        String policyId = cleanText.substring(startId, endId);
        System.out.println("2. Extracted ID: " + policyId);

        // Extract the holder's name (between "HOLDER: " and " |")
        int startHolder = cleanText.indexOf("HOLDER: ") + 8;
        int endHolder = cleanText.indexOf(" |", startHolder);
        String holderName = cleanText.substring(startHolder, endHolder);
        System.out.println("   Extracted Holder: " + holderName);

        // 3. PARSING: Turn text into numbers
        // Find where "COST: " ends to get the number part
        int startCost = cleanText.indexOf("COST: ") + 6;
        String costText = cleanText.substring(startCost);

        // Convert the text "1200" into an integer
        int costNumber = Integer.parseInt(costText);
        System.out.println("3. Parsed Cost: $" + costNumber);

        // Calculate a 10% tax
        int tax = costNumber / 10;
        int total = costNumber + tax;
        System.out.println("   Total with Tax: $" + total);

        // 4. MANIPULATION: Build a new formatted sentence
        // StringBuilder helps join pieces together easily
        StringBuilder report = new StringBuilder();
        report.append("Policy Report for: ");
        report.append(holderName); // Add name
        report.append(" (ID: ");
        report.append(policyId);   // Add ID
        report.append(") - Total Due: $");
        report.append(total);      // Add calculated total

        String finalMessage = report.toString();
        System.out.println("4. Formatted Output: " + finalMessage);
    }
}   