class BackupThread extends Thread {
    private static List<String> getClassNames(String className) {
        List<String> retr = new ArrayList<String>();
        try {
            ClassLoader classLoader = SPIUtil.class.getClassLoader();
            Enumeration<URL> spiUrls = classLoader.getResources("META-INF/service/" + className);
            while (spiUrls.hasMoreElements()) {
                URL url = spiUrls.nextElement();
                InputStream stream = (InputStream) url.openConnection().getContent();
                if (stream == null) continue;
                StringBuffer out = new StringBuffer();
                byte[] b = new byte[4096];
                for (int n; (n = stream.read(b)) != -1; ) {
                    out.append(new String(b, 0, n));
                }
                String[] items = out.toString().split("\n");
                for (int i = 0; i < items.length; i++) retr.add(items[i].trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retr;
    }
}
