class BackupThread extends Thread {
    private byte[] getClassData(File f) {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1) out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        } finally {
            if (stream != null) try {
                stream.close();
            } catch (IOException e1) {
            }
        }
        return null;
    }
}
