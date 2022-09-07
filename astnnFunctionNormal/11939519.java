class BackupThread extends Thread {
    @Test
    public void putGetSize() {
        HugeMapBuilder<HandTypesKey, HandTypes> dummy = new HugeMapBuilder<HandTypesKey, HandTypes>() {

            {
                allocationSize = 64 * 1024;
                setRemoveReturnsNull = true;
            }
        };
        HandTypesMap map = new HandTypesMap(dummy);
        HandTypesKeyImpl key = new HandTypesKeyImpl();
        HandTypesImpl value = new HandTypesImpl();
        long start = System.nanoTime();
        final int size = 5000000;
        for (int i = 0; i < size; i += 2) {
            put(map, key, value, i, false);
            put(map, key, value, i, true);
        }
        for (int i = 0; i < size; i += 2) {
            get(map, key, i, false);
            get(map, key, i, true);
        }
        for (Map.Entry<HandTypesKey, HandTypes> entry : map.entrySet()) {
            assertEquals(entry.getKey().getInt(), entry.getValue().getInt());
            assertEquals(entry.getKey().getBoolean(), entry.getValue().getBoolean());
        }
        long time = System.nanoTime() - start;
        System.out.printf("Took an average of %,d ns to write/read", time / size);
        System.out.println(Arrays.toString(map.sizes()));
        System.out.println(Arrays.toString(map.capacities()));
    }
}
