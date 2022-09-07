class BackupThread extends Thread {
    public static void init() throws IOException {
        if (definitions != null) {
            throw new IllegalStateException("Definitions already loaded.");
        }
        logger.info("Loading definitions...");
        RandomAccessFile raf = new RandomAccessFile("data/itemDefinitions.bin", "r");
        try {
            ByteBuffer buffer = raf.getChannel().map(MapMode.READ_ONLY, 0, raf.length());
            int count = buffer.getShort() & 0xFFFF;
            definitions = new ItemDefinition[count];
            for (int i = 0; i < count; i++) {
                String name = Buffers.readString(buffer);
                String examine = Buffers.readString(buffer);
                boolean noted = buffer.get() == 1 ? true : false;
                int parentId = buffer.getShort() & 0xFFFF;
                if (parentId == 65535) {
                    parentId = -1;
                }
                boolean noteable = buffer.get() == 1 ? true : false;
                int notedId = buffer.getShort() & 0xFFFF;
                if (notedId == 65535) {
                    notedId = -1;
                }
                boolean stackable = buffer.get() == 1 ? true : false;
                boolean members = buffer.get() == 1 ? true : false;
                boolean prices = buffer.get() == 1 ? true : false;
                int shop = -1;
                int highAlc = -1;
                int lowAlc = -1;
                if (prices) {
                    shop = buffer.getInt();
                    highAlc = (int) (shop * 0.6D);
                    lowAlc = (int) (shop * 0.4D);
                }
                int[] bonuses = new int[12];
                for (int index = 0; index < 12; index++) {
                    bonuses[index] = buffer.getShort();
                }
                definitions[i] = new ItemDefinition(i, name, examine, noted, noteable, stackable, parentId, notedId, members, shop, highAlc, lowAlc, bonuses);
            }
            logger.info("Loaded " + definitions.length + " definitions.");
        } finally {
            raf.close();
        }
    }
}
