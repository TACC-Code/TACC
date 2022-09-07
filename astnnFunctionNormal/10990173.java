class BackupThread extends Thread {
    @SuppressWarnings({ "unchecked", "deprecation" })
    public void run() {
        try {
            URLConnection con1 = url.openConnection();
            con1.connect();
            @SuppressWarnings("unused") InputStream is1 = con1.getInputStream();
        } catch (IOException e) {
            if (e.getMessage().contains("response code: 500")) {
                log("Server {" + url.toString() + "} is still alive!");
            } else {
                log("Server {" + url.toString() + "} is dead! ... Stopping thread, uninstalling and removing service!");
                ProxyUtils.removeProxyInstance(messageId);
                runner.stop();
            }
        }
    }
}
