class BackupThread extends Thread {
    public static void SHAselfTest(Digest s) {
        int i, j;
        System.out.println("SHA-1 Test PROGRAM.");
        System.out.println("This code runs the test vectors through the code.");
        System.out.println("First test is 'abc'");
        String z = "abc";
        s.update((byte) 'a');
        s.update((byte) 'b');
        s.update((byte) 'c');
        System.out.println(Fields.bytesToHex(s.digest()).toUpperCase());
        System.out.println("A9993E364706816ABA3E25717850C26C9CD0D89D");
        System.out.println("Next Test is 'abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq'");
        z = "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq";
        for (i = 0; i < z.length(); ++i) {
            s.update((byte) z.charAt(i));
        }
        System.out.println(Fields.bytesToHex(s.digest()).toUpperCase());
        System.out.println("84983E441C3BD26EBAAE4AA1F95129E5E54670F1");
        long startTime = 0 - System.currentTimeMillis();
        System.out.println("Last test is 1 million 'a' characters.");
        for (i = 0; i < 1000000; i++) s.update((byte) 'a');
        System.out.println(Fields.bytesToHex(s.digest()).toUpperCase());
        System.out.println("34AA973CD4C4DAA4F61EEB2BDBAD27316534016F");
        startTime += System.currentTimeMillis();
        double d = ((double) startTime) / 1000.0;
        System.out.println(" done, elapsed time = " + d);
    }
}
