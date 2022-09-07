class BackupThread extends Thread {
    private byte[] loadClassBytes(URL url) {
        final List collBytes = new ArrayList();
        final List collLen = new ArrayList();
        final byte[] classBytes;
        InputStream is = null;
        byte[] b;
        int len;
        int totalLen = 0;
        try {
            is = url.openStream();
            do {
                b = new byte[4096];
                len = is.read(b);
                if (len > 0) {
                    collBytes.add(b);
                    collLen.add(new Integer(len));
                    totalLen += len;
                }
            } while (len > 0);
            is.close();
            classBytes = new byte[totalLen];
            for (int i = 0, off = 0; i < collBytes.size(); i++) {
                len = ((Integer) collLen.get(i)).intValue();
                b = (byte[]) collBytes.get(i);
                System.arraycopy(b, 0, classBytes, off, len);
                off += len;
            }
            return classBytes;
        } catch (IOException e1) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e11) {
                }
            }
            return null;
        }
    }
}
