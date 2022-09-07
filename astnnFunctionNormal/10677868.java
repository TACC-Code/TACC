class BackupThread extends Thread {
        private Object getNextElement() {
            return AccessController.doPrivileged(new PrivilegedAction() {

                public Object run() {
                    while (urls.hasMoreElements()) {
                        try {
                            return ((URL) urls.nextElement()).openStream();
                        } catch (IOException e) {
                        }
                    }
                    return null;
                }
            });
        }
}
