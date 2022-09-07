class BackupThread extends Thread {
    private File writeTemp(Reader reader) throws IOException {
        File temp = File.createTempFile("jep", ".py");
        FileWriter fout = new FileWriter(temp);
        char[] buf = new char[1024];
        int count;
        while ((count = reader.read(buf)) > 0) fout.write(buf, 0, count);
        fout.close();
        return temp;
    }
}
