class BackupThread extends Thread {
    public static byte[] readResource(String resource, Class c, boolean printError) {
        try {
            InputStream inputStream = null;
            System.err.println("read resource:" + resource);
            if (!resource.startsWith("http:")) {
                inputStream = c.getResourceAsStream(resource);
            }
            if (inputStream == null) {
                URL url = new URL(resource);
                System.err.println("read url:" + url);
                URLConnection connection = url.openConnection();
                inputStream = connection.getInputStream();
            }
            byte[] buffer = new byte[100000];
            byte[] bytes = new byte[10];
            int totalBytes = 0;
            while (true) {
                int howMany = inputStream.read(buffer);
                if (howMany < 0) {
                    break;
                }
                while ((howMany + totalBytes) > bytes.length) {
                    byte[] tmp = bytes;
                    bytes = new byte[tmp.length * 2];
                    System.arraycopy(tmp, 0, bytes, 0, totalBytes);
                }
                System.arraycopy(buffer, 0, bytes, totalBytes, howMany);
                totalBytes += howMany;
            }
            byte[] finalBytes = new byte[totalBytes];
            System.arraycopy(bytes, 0, finalBytes, 0, totalBytes);
            return finalBytes;
        } catch (Exception exc) {
            if (printError) {
                System.err.println("Error reading resource:" + resource + " " + exc);
                exc.printStackTrace();
            }
        }
        return null;
    }
}
