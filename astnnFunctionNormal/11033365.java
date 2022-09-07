class BackupThread extends Thread {
    public static void genericBenchTest(Digest sha1) {
        System.out.println("\nBegin benchmark");
        long size = 4096;
        long count = 100000;
        byte data[] = createDummyData((int) size);
        long startSameUpdate = System.currentTimeMillis();
        for (int i = 0; i < count; i++) sha1.update(data);
        long endSameUpdate = System.currentTimeMillis();
        byte digested[] = sha1.digest();
        long afterDigested = System.currentTimeMillis();
        long updateTime = endSameUpdate - startSameUpdate;
        long speed = 0;
        long eachTime = 0;
        if (updateTime > 0) {
            speed = size * count * 1000 / updateTime;
            eachTime = count / updateTime;
        }
        System.out.println("SHA update " + count + " times for the same data block of size " + size + " bytes:");
        System.out.println(updateTime + "ms [" + speed + "bytes/second, " + eachTime + "updates/ms]\n");
        count = 1000;
        byte data2[][] = new byte[(int) count][];
        for (int i = 0; i < count; i++) data2[i] = createDummyData((int) size);
        long differentStartTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) sha1.update(data2[i]);
        long endDifferentUpdate = System.currentTimeMillis();
        digested = sha1.digest();
        afterDigested = System.currentTimeMillis();
        updateTime = endDifferentUpdate - differentStartTime;
        speed = 0;
        eachTime = 0;
        if (updateTime > 0) {
            speed = size * count * 1000 / updateTime;
            eachTime = count / updateTime;
        }
        System.out.println("SHA update " + count + " times for different data blocks of size " + size + " bytes:");
        System.out.println(updateTime + "ms [" + speed + "bytes/second, " + eachTime + "ms/update]");
        System.out.println("\nEnd benchmark");
    }
}
