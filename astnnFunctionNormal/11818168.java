class BackupThread extends Thread {
    public boolean readAndWrite(Reader reader, Writer writer, int length) {
        if (length <= 0) length = 4096;
        char buf[] = new char[length];
        try {
            while (true) {
                int i = reader.read(buf);
                if (i <= 0) break;
                writer.write(buf, 0, i);
            }
            reader.close();
            writer.flush();
            writer.close();
            return true;
        } catch (java.io.IOException e) {
            return false;
        }
    }
}
