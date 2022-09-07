class BackupThread extends Thread {
    public static void testtimetrial() {
        int test_block_len = 1000, test_block_count = 10000;
        byte block[] = new byte[test_block_len], hash[];
        int i;
        long start, end;
        MD5 md5;
        System.out.print("MD5 time trial. Digesting " + test_block_count + " " + test_block_len + "-byte blocks ...");
        for (i = 0; i < block.length; i++) {
            block[i] = (byte) (i & 0xff);
        }
        md5 = new MD5();
        start = System.currentTimeMillis();
        for (i = 0; i < test_block_count; i++) md5.update(block, test_block_len);
        hash = md5.digest();
        end = System.currentTimeMillis();
        System.out.print(" done\nDigest = " + md5.asHex() + "\nTime = " + (end - start) + " milliseconds\n");
    }
}
