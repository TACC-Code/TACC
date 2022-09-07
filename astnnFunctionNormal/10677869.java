class BackupThread extends Thread {
                public Object run() {
                    while (urls.hasMoreElements()) {
                        try {
                            return ((URL) urls.nextElement()).openStream();
                        } catch (IOException e) {
                        }
                    }
                    return null;
                }
}
