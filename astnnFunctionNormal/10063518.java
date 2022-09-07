class BackupThread extends Thread {
    public byte[] loadClassFirst(final String className) {
        if (className.equals("com.sun.sgs.impl.util.Exporter")) {
            final URL url = Thread.currentThread().getContextClassLoader().getResource("com/sun/sgs/impl/util/Exporter.class.bin");
            if (url != null) {
                try {
                    return StreamUtil.read(url.openStream());
                } catch (final IOException e) {
                }
            }
            throw new IllegalStateException("Unable to load Exporter.class.bin");
        }
        return null;
    }
}
