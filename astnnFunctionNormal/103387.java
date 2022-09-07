class BackupThread extends Thread {
    public String writer__read(String rFilePath) throws Exception {
        File file = new File(rFilePath);
        FileReader in = new FileReader(file);
        int size = (int) file.length();
        char[] data = new char[size];
        int charsRead = 0;
        while (charsRead < size) {
            charsRead += in.read(data, charsRead, size - charsRead);
        }
        return new String(data);
    }
}
