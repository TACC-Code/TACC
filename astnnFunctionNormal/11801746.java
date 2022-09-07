class BackupThread extends Thread {
                public Source resolve(String base, String href) {
                    try {
                        URL url = sourceResolver.resolve(href);
                        if (url != null) {
                            return new StreamSource(url.openStream(), url.toExternalForm());
                        }
                    } catch (Exception ex) {
                        log.warn("Unable to resolve url " + href);
                    }
                    return null;
                }
}
