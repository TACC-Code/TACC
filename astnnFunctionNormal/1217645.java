class BackupThread extends Thread {
    public File write2SDFromInput(String path, String fileName, InputStream input) {
        File file = null;
        FileOutputStream fos = null;
        try {
            creatSDDir(path);
            file = creatSDFile(path + fileName);
            fos = new FileOutputStream(file);
            byte buf[] = new byte[128];
            do {
                int numread = input.read(buf);
                if (numread <= 0) {
                    break;
                }
                fos.write(buf, 0, numread);
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
