class BackupThread extends Thread {
    public static Mix createFromFile(String pathFile) throws FileNotFoundException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Mix mix = new Mix();
        FileInputStream fis = new FileInputStream(pathFile);
        FileChannel fc = fis.getChannel();
        mix.header.readFromFileChannel(fc);
        mix.readCommonParameters(fc);
        return mix;
    }
}
