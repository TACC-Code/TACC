class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    private void doJavaSerializationTest(Stats stats, boolean deltas) throws Exception {
        Map<String, Object> map = CollectionFactory.newMap();
        Map<String, Object> resultMap = null;
        for (int i = 0; i < integerFields.length; i++) {
            map.put(integerFields[i].getIdentity(), integerFields[i].get());
        }
        for (int i = 0; i < doubleFields.length; i++) {
            map.put(doubleFields[i].getIdentity(), doubleFields[i].get());
        }
        for (int i = 0; i < ITERATIONS; i++) {
            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            ObjectOutputStream outStream = new ObjectOutputStream(byteOutput);
            stats.start("write-java", System.nanoTime());
            outStream.writeObject(map);
            stats.stop("write-java", System.nanoTime());
            final byte[] byteArray = byteOutput.toByteArray();
            ObjectInputStream inStream = new ObjectInputStream(new ByteArrayInputStream(byteArray));
            stats.start("read-java", System.nanoTime());
            resultMap = (Map<String, Object>) inStream.readObject();
            stats.stop("read-java", System.nanoTime());
            stats.dataSize(byteArray.length);
        }
        stats.setCount(ITERATIONS);
        assertNotSame("Failed to write/read resultMap", map, resultMap);
        assertEquals("Failed to write/read resultMap", map, resultMap);
    }
}
