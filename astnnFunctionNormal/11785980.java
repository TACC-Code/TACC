class BackupThread extends Thread {
    public void writeToFile(String pathfile) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(pathfile);
        FileChannel fc = fos.getChannel();
        header.writeToFileChannel(fc);
        this.writeCommonParameters(fc);
        fc.close();
        fos.close();
    }
}
