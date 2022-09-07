class BackupThread extends Thread {
    public static void submitLog(String message, String detail, String proxyAddress, int proxyPort) {
        String user = System.getProperty("user.name");
        String system = System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") " + System.getProperty("os.version");
        String java = System.getProperty("java.vm.name") + " " + System.getProperty("java.vm.version");
        String version = AboutNakedObjects.getVersion() + " " + AboutNakedObjects.getBuildId();
        try {
            URL url = proxyAddress == null ? new URL(URL_SPEC) : new URL("http", proxyAddress, proxyPort, URL_SPEC);
            LOG.info("connect to " + url);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            HttpQueryWriter out = new HttpQueryWriter(connection.getOutputStream());
            out.addParameter("user", user);
            out.addParameter("system", system);
            out.addParameter("vm", java);
            out.addParameter("error", message);
            out.addParameter("trace", detail);
            out.addParameter("version", version);
            out.close();
            InputStream in = connection.getInputStream();
            int c;
            StringBuffer result = new StringBuffer();
            while ((c = in.read()) != -1) {
                result.append((char) c);
            }
            LOG.info(result);
            in.close();
        } catch (UnknownHostException e) {
            LOG.info("could not find host (unknown host) to submit log to");
        } catch (IOException e) {
            LOG.debug("i/o problem submitting log", e);
        }
    }
}
