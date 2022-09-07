class BackupThread extends Thread {
    private static int sceGuSetMatrix4x4(IMemoryWriter listWriter, IMemoryReader matrixReader, int startCmd, int matrixCmd, int index) {
        listWriter.writeNext((startCmd << 24) + index);
        int cmd = matrixCmd << 24;
        for (int i = 0; i < 16; i++) {
            listWriter.writeNext(cmd | (matrixReader.readNext() >>> 8));
        }
        return 68;
    }
}
