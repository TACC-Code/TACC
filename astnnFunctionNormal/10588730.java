class BackupThread extends Thread {
    public XULViewer() {
        super("XUL Viewer");
        URL url = Thread.currentThread().getContextClassLoader().getResource("org/xul/script/resources/xul.properties");
        try {
            PropertyResourceBundle prb = new PropertyResourceBundle(url.openStream());
            String version = prb.getString("version");
            System.out.println("XULViewer version " + version);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.setLayout(new BorderLayout());
        this.add(pane);
        constructPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
    }
}
