public class SDES {
    // Permutation tables
    public static final int[] P10_TABLE = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    public static final int[] P8_TABLE = {6, 3, 7, 4, 8, 5, 10, 9};
    private static final int[] P4_TABLE = {2, 4, 3, 1};
    private static final int[] IP_TABLE = {2, 6, 3, 1, 4, 8, 5, 7};
    private static final int[] EP_TABLE = {4, 1, 2, 3, 2, 3, 4, 1};
    private static final int[] IP_NI_TABLE = {4, 1, 3, 5, 7, 2, 8, 6};

    // S-boxes
    private static final int[][] SBOX0 = {
            {1, 0, 3, 2},
            {3, 2, 1, 0},
            {0, 2, 1, 3},
            {3, 1, 0, 2}
    };
    private static final int[][] SBOX1 = {
            {0, 1, 2, 3},
            {2, 3, 1, 0},
            {3, 0, 1, 2},
            {2, 1, 0, 3}
    };

    public static final int KEY_LENGTH = 10;
    public static final int DATA_LENGTH = 8;
    
    // Perform a permutation
    private static String permute(String inputStr, int[] table) {
        StringBuilder outputStr = new StringBuilder();
        for (int bitPosition : table) {
            outputStr.append(inputStr.charAt(bitPosition - 1));
        }
        return outputStr.toString();
    }

    // Perform a left shift
    private static String ls(String key, int n) {
        String leftHalf = key.substring(0, 5);
        String rightHalf = key.substring(5);
        String shiftedLeft = leftHalf.substring(n) + leftHalf.substring(0, n);
        String shiftedRight = rightHalf.substring(n) + rightHalf.substring(0, n);
        return shiftedLeft + shiftedRight;
    }

    // Generate subkeys
    public static String[] subkey(String k, int[] p10Table, int[] p8Table) {
        String p10Key = permute(k, p10Table);
        String k1 = permute(ls(p10Key, 1), p8Table);
        String k2 = permute(ls(ls(p10Key, 1), 1), p8Table);
        return new String[]{k1, k2};
    }

    // The function used in the Fk function
    private static String function(String rightHalf, String k, int[][] sbox0, int[][] sbox1, int[] p4Table) {
        // Expansion and XOR
        String expanded = permute(rightHalf, EP_TABLE);
        int xored = Integer.parseInt(expanded, 2) ^ Integer.parseInt(k, 2);
        String xoredStr = String.format("%08d", Integer.parseInt(Integer.toBinaryString(xored)));

        // S-box substitutions
        String s0Input = xoredStr.substring(0, 4);
        String s1Input = xoredStr.substring(4);
        int s0Row = Integer.parseInt(s0Input.charAt(0) + "" + s0Input.charAt(3), 2);
        int s0Col = Integer.parseInt(s0Input.substring(1, 3), 2);
        int s1Row = Integer.parseInt(s1Input.charAt(0) + "" + s1Input.charAt(3), 2);
        int s1Col = Integer.parseInt(s1Input.substring(1, 3), 2);
        String s0Output = String.format("%02d", Integer.parseInt(Integer.toBinaryString(sbox0[s0Row][s0Col])));
        String s1Output = String.format("%02d", Integer.parseInt(Integer.toBinaryString(sbox1[s1Row][s1Col])));
        String sOutput = s0Output + s1Output;

        // Permutation
        return permute(sOutput, p4Table);
    }

    // Encrypt plaintext using S-DES
    public static String encrypt(String p, String k) {

        String k1, k2;
        String[] subkeys = subkey(k, P10_TABLE, P8_TABLE);
        k1 = subkeys[0];
        k2 = subkeys[1];

        // Initial permutation
        p = permute(p, IP_TABLE);
        String l0 = p.substring(0, 4);
        String r0 = p.substring(4);
        String l1 = r0;

        // First round
        String fResult = function(r0, k1, SBOX0, SBOX1, P4_TABLE);
        int r1Int = Integer.parseInt(l0, 2) ^ Integer.parseInt(fResult, 2);
        String r1 = String.format("%04d", Integer.parseInt(Integer.toBinaryString(r1Int)));

        // Second round
        fResult = function(r1, k2, SBOX0, SBOX1, P4_TABLE);
        int r2Int = Integer.parseInt(l1, 2) ^ Integer.parseInt(fResult, 2);
        String r2 = String.format("%04d", Integer.parseInt(Integer.toBinaryString(r2Int)));

        // Final permutation
        return permute(r2 + r1, IP_NI_TABLE);
    }

    // Decrypt ciphertext using S-DES
    public static String decrypt(String c, String k) {

        String k1, k2;
        String[] subkeys = subkey(k, P10_TABLE, P8_TABLE);
        k1 = subkeys[0];
        k2 = subkeys[1];

        // Initial permutation
        c = permute(c, IP_TABLE);
        String r2 = c.substring(0, 4);
        String l2 = c.substring(4);

        // First round
        String fResult = function(l2, k2, SBOX0, SBOX1, P4_TABLE);
        int l1Int = Integer.parseInt(r2, 2) ^ Integer.parseInt(fResult, 2);
        String l1 = String.format("%04d", Integer.parseInt(Integer.toBinaryString(l1Int)));

        // Second round
        fResult = function(l1, k1, SBOX0, SBOX1, P4_TABLE);
        int r1Int = Integer.parseInt(l2, 2) ^ Integer.parseInt(fResult, 2);
        String r1 = String.format("%04d", Integer.parseInt(Integer.toBinaryString(r1Int)));

        // Final permutation
        return permute(r1 + l1, IP_NI_TABLE);
    }
    public static String encryptAsc(String p, String k) {
        Test test=new Test();
        String mi="";
        for(int i=0;i<p.length();i++){   
            int ascIn=Integer.valueOf(p.charAt(i));  
            String in=test.ToBinary(ascIn,8);
            String out=encrypt(in, k);
            char ascOut=(char)Integer.parseInt(out,2);
            mi+=ascOut;
        }
        return mi;
    }
    public static String decryptAsc(String c, String k) {
        Test test=new Test();
        String ming="";
        for(int i=0;i<c.length();i++){   
            int ascIn=Integer.valueOf(c.charAt(i));  
            String in=test.ToBinary(ascIn,8);
            String out=decrypt(in, k);
            char ascOut=(char)Integer.parseInt(out,2);
            ming+=ascOut;
        }
        return ming;
    }
}
