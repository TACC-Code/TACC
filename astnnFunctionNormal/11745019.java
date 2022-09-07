class BackupThread extends Thread {
    @Test
    public void testSearchAndUpdateCollectionSerialization() throws IOException, ClassNotFoundException {
        gcPrintUsed();
        int length = 1000 * 1000;
        List<byte[]> mts = new ArrayList<byte[]>();
        HugeArrayBuilder<MutableTypes> mtb = new HugeArrayBuilder<MutableTypes>() {
        };
        final MutableTypes bean = mtb.createBean();
        bean.setBoolean2(true);
        bean.setByte2((byte) 1);
        byte[] bytes = toBytes(bean);
        for (int i = 0; i < length; i++) mts.add(bytes.clone());
        System.out.println("Per object size is " + (4 + (bytes.length + 7 + 12) / 8 * 8));
        gcPrintUsed();
        long start = System.nanoTime();
        for (int i = 0, mtsSize = mts.size(); i < mtsSize; i++) {
            MutableTypes mt = (MutableTypes) fromBytes(mts.get(i));
            mt.setInt(mt.getInt() + 1);
            mts.set(i, toBytes(mt));
        }
        long time = System.nanoTime() - start;
        printUsed();
        System.out.printf("List<byte[]> with readObject/writeObject update one field took an average %,d ns.%n", time / length);
    }
}
