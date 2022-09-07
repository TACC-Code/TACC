class BackupThread extends Thread {
    static void copy(InputStream fis, OutputStream fos) {
        try {
            byte[] buffer = new byte[0xFFFF];
            for (int len; (len = fis.read(buffer)) != -1; ) fos.write(buffer, 0, len);
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            if (fis != null) try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (fos != null) try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
