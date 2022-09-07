class BackupThread extends Thread {
    private final synchronized URL findResource(final String name, final boolean forwardToParent) {
        try {
            if (name == null) {
                return null;
            }
            if (name.startsWith("/")) {
                try {
                    this.clMutex.acquire();
                    final URL ret = this.addedURL.get(name);
                    if (ret != null) {
                        return ret;
                    }
                } finally {
                    this.clMutex.release();
                }
            } else {
                try {
                    this.clMutex.acquire();
                    final URL ret = this.addedURL.get("/" + name);
                    if (ret != null) {
                        return ret;
                    }
                } finally {
                    this.clMutex.release();
                }
            }
            if (this.fsLabel != null) {
                final URL url = Handler.URL(this.fsLabel, (name.startsWith("/") ? name : ("/" + name)));
                url.openConnection().getInputStream().close();
                return url;
            }
        } catch (final Exception e) {
        }
        if (forwardToParent) {
            ClassLoader parent = null;
            if ((parent = this.getParent()) != null) {
                return parent.getResource(name);
            } else {
                return Dynamic._.SystemClassLoader().getResource(name);
            }
        }
        return null;
    }
}
