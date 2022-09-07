class BackupThread extends Thread {
    byte[] getJar(String address) {
        byte[] data;
        boolean gte133 = version().compareTo("1.33u") >= 0;
        try {
            URL url = new URL(address);
            IJ.showStatus("Connecting to " + IJ.URL);
            URLConnection uc = url.openConnection();
            int len = uc.getContentLength();
            String name = address.endsWith("ij/ij.jar") ? "daily build" : "ij.jar";
            IJ.showStatus("Downloading ij.jar (" + IJ.d2s((double) len / 1048576, 1) + "MB)");
            InputStream in = uc.getInputStream();
            data = new byte[len];
            int n = 0;
            while (n < len) {
                int count = in.read(data, n, len - n);
                if (count < 0) throw new EOFException();
                n += count;
                if (gte133) IJ.showProgress(n, len);
            }
            in.close();
        } catch (IOException e) {
            return null;
        }
        return data;
    }
}
