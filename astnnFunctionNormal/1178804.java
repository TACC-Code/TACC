class BackupThread extends Thread {
    private static int sceGuSetMatrix4x3(IMemoryWriter listWriter, IMemoryReader matrixReader, int startCmd, int matrixCmd, int index) {
        listWriter.writeNext((startCmd << 24) + index);
        int cmd = matrixCmd << 24;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                listWriter.writeNext(cmd | (matrixReader.readNext() >>> 8));
            }
            matrixReader.skip(1);
        }
        return 52;
    }
}
