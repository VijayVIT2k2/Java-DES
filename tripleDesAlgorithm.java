import java.util.Random;
// 20MIC0008
// Vijay. P
// 08/ 09/ 2022

class tripleDesAlgorithm {
    private static int[] permutationChoice1;
    private static int[] IniPer;
    private static int[] invIniPer;
    private static int[] permutationChoice2;
    private static int[] straightPermutation;
    private static int[] expandedPermutation;
    private static int[][][] substBox;
    private static Random ran = new Random();
    private static class DES {
        int[] shiftBits = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };
        private String cipherText;
        private String plainText;
        String toBinary(String input) {
            int n = input.length() * 4;
            input = Long.toBinaryString(Long.parseUnsignedLong(input, 16));
            while (input.length() < n) input = "0" + input;
            return input;
        }
        String toHexaDecimal(String input) {
            int n = input.length() / 4;
            input = Long.toHexString(Long.parseUnsignedLong(input, 2));
            while (input.length() < n) input = "0" + input;
            return input;
        }
        String permutation(int[] sequence, String input) {
            String output = "";
            input = toBinary(input);
            for (int i = 0; i < sequence.length; i++) output += input.charAt(sequence[i] - 1);
            output = toHexaDecimal(output);
            return output;
        }
        String xorOperation(String a, String b) {
            long t_a = Long.parseUnsignedLong(a, 16);
            long t_b = Long.parseUnsignedLong(b, 16);
            t_a = t_a ^ t_b;
            a = Long.toHexString(t_a);
            while (a.length() < b.length()) a = "0" + a;
            return a;
        }
        String Shift(String input, int numBits) {
            int n = input.length() * 4;
            int perm[] = new int[n];
            for (int i = 0; i < n - 1; i++) perm[i] = (i + 2);
            perm[n - 1] = 1;
            while (numBits-- > 0) input = permutation(perm, input);
            return input;
        }
        String[] getKeys(String key) {
            String keys[] = new String[16];
            key = permutation(permutationChoice1, key);
            for (int i = 0; i < 16; i++) {
                key = Shift(
                        key.substring(0, 7), shiftBits[i])
                        + Shift(key.substring(7, 14),
                        shiftBits[i]);
                keys[i] = permutation(permutationChoice2, key);
            }
            return keys;
        }
        String sBox(String input) {
            String output = "";
            input = toBinary(input);
            for (int i = 0; i < 48; i += 6) {
                String temp = input.substring(i, i + 6);
                int num = i / 6;
                int row = Integer.parseInt(
                        temp.charAt(0) + "" + temp.charAt(5), 2);
                int col = Integer.parseInt(
                        temp.substring(1, 5), 2);
                output += Integer.toHexString(
                        substBox[num][row][col]);
            }
            return output;
        }
        String round(String input, String key, int num) {
            String leftSubString = input.substring(0, 8);
            String temp = input.substring(8, 16);
            String rightSubString = temp;
            temp = permutation(expandedPermutation, temp);
            temp = xorOperation(temp, key);
            temp = sBox(temp);
            temp = permutation(straightPermutation, temp);
            leftSubString = xorOperation(leftSubString, temp);

            System.out.println("Round "
                    + (num + 1) + " "
                    + rightSubString.toUpperCase()
                    + " " + leftSubString.toUpperCase() + " "
                    + key.toUpperCase());
            return rightSubString + leftSubString;
        }
        String encryption(String text, String key) {
            int i;
            String keys[] = getKeys(key);
            text = permutation(IniPer, text);
            System.out.println(
                    "After initial permutation: "
                            + text.toUpperCase());
            System.out.println(
                    "After splitting: L0="
                            + text.substring(0, 8).toUpperCase()
                            + " R0="
                            + text.substring(8, 16).toUpperCase() + "\n");
            System.out.println("Iteration | Right | Left | Key");
            for (i = 0; i < 16; i++) text = round(text, keys[i], i);
            text = text.substring(8, 16) + text.substring(0, 8);
            cipherText = permutation(invIniPer, text);
            return cipherText;
        }
        String decryption(String text, String key) {
            int i;
            String keys[] = getKeys(key);
            text = permutation(IniPer, text);
            System.out.println(
                    "After initial permutation: "
                            + text.toUpperCase());
            System.out.println(
                    "After splitting: L0="
                            + text.substring(0, 8).toUpperCase()
                            + " R0="
                            + text.substring(8, 16).toUpperCase() + "\n");
            System.out.println("Iteration | Right | Left | Key");
            for (i = 15; i > -1; i--) text = round(text, keys[i], 15 - i);
            text = text.substring(8, 16) + text.substring(0, 8);
            plainText = permutation(invIniPer, text);
            return plainText;
        }
    }
    static int[] inversePermutation(int arr[], int size) {
        int invArr[] = new int[size];
        for (int i = 0; i < size; i++)
            invArr[arr[i] - 1] = i + 1;
       return invArr;
    }
    private static boolean check(int[] arr, int randNumCheck) {
            for (int x : arr) if (x == randNumCheck) return true; return false;
    }
    public static void main(String args[]) {

        IniPer = new int[64];
        boolean flag;
        for(int i = 0; i < 64; i++){
            int temp = ran.nextInt(64) + 1;
            flag = check(IniPer,temp);
            if(flag == true)i--; else IniPer[i] = temp;
        }
        invIniPer = inversePermutation(IniPer, IniPer.length);
        permutationChoice1 = new int[56];
        for(int i = 0; i < 56; i++){
            int temp = ran.nextInt(56) + 1;
            flag = check(permutationChoice1,temp);
            if(flag == true) i--; else permutationChoice1[i] = temp;
        }
        permutationChoice2 = new int[48];
        for(int i = 0; i < 48; i++) {
            int temp = ran.nextInt(48) + 1;
            flag = check(permutationChoice2,temp);
            if(flag == true) i--; else permutationChoice2[i] = temp;
        }
        expandedPermutation = new int[48];
        for(int i = 0; i < 48; i++) {
            int temp = ran.nextInt(32) + 1;
            expandedPermutation[i] = temp;
        }
        straightPermutation = new int[32];
        for(int i = 0; i < 32; i++) {
            int temp = ran.nextInt(32) + 1;
            flag = check(straightPermutation,temp);
            if(flag == true) i--; else straightPermutation[i] = temp;
        }
        substBox = new int[8][4][16];
        for(int i = 0; i < 8; i++) for(int j = 0; j < 4; j++) for(int k = 0; k < 16; k++) {
                    int temp = ran.nextInt(16) + 1;
                    flag = check(substBox[i][j],temp);
                    if(flag == true) k--; else substBox[i][j][k] = temp;
                }
        String text = "A123B45C6D78E90F";
        String key1 = "01234ABCDEF56789";
        String key2 = "ABCDEF0123456789";
        String key3 = "0123456789ABCDEF";
        DES cipher = new DES();

        System.out.println(String.format("Message: %s\nKey 1: %s\nKey2: %s\nKey3: %s", text , key1, key2, key3));

        System.out.println("Encryption:\n");
        text = cipher.encryption(text, key1);
        System.out.println("\nCipher Text after encrypting using key1: "
                + text.toUpperCase() + "\n");
        text = cipher.decryption(text, key2);
        System.out.println("\nCipher Text after decrypting using key2: "
                + text.toUpperCase() + "\n");
        text = cipher.encryption(text, key3);
        System.out.println("\nCipher Text after encrypting using key3: "
                + text.toUpperCase() + "\n");

        System.out.println(String.format("\nCiphertext output after encryption using triple DES algorithm: %s\n", text.toUpperCase()));

        System.out.println("Decryption:\n");
        text = cipher.decryption(text, key3);
        System.out.println("\nPlain Text after decrypting using key 3: "
                + text.toUpperCase() + "\n");
        text = cipher.encryption(text, key2);
        System.out.println("\nPlain Text after encrypting using key 2: "
                + text.toUpperCase() + "\n");
        text = cipher.decryption(text, key1);
        System.out.println("\nPlain Text after decrypting using key 1: "
                + text.toUpperCase() + "\n");

        System.out.println(String.format("\nPlaintext output after decryption using triple DES algorithm: %s\n", text.toUpperCase()));
    }
}
