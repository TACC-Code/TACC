class BackupThread extends Thread {
    public static void loadInitialResources() {
        try {
            ClassLoader cl = new Pooka().getClass().getClassLoader();
            java.net.URL url;
            if (cl == null) {
                url = ClassLoader.getSystemResource("net/suberic/pooka/Pookarc");
            } else {
                url = cl.getResource("net/suberic/pooka/Pookarc");
            }
            if (url == null) {
                url = new Pooka().getClass().getResource("/net/suberic/pooka/Pookarc");
            }
            java.io.InputStream is = url.openStream();
            VariableBundle resources = new net.suberic.util.VariableBundle(is, "net.suberic.pooka.Pooka");
            sManager.setResources(resources);
        } catch (Exception e) {
            System.err.println("caught exception loading system resources:  " + e);
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
