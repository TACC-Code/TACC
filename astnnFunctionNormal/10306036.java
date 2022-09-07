class BackupThread extends Thread {
    @Override
    protected Integer doInBackground() throws UnavailableServiceException, IOException {
        bs = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
        loc = bs.getCodeBase().toString() + ".*";
        DownloadService2 ds2 = (DownloadService2) ServiceManager.lookup("javax.jnlp.DownloadService2");
        ResourceSpec spec = new ResourceSpec(loc, null, ds2.ALL);
        setMessage("doing update check...");
        final List<URL> updates = new ArrayList();
        ResourceSpec[] resources = ds2.getCachedResources(spec);
        int counter = 1;
        totalUpdates = 100;
        for (ResourceSpec rs : resources) {
            setProgress(counter++, 0, resources.length);
            URL url = new URL(rs.getUrl());
            if (rs.getUrl().endsWith("jnlp")) {
                jnlp = url;
            }
            System.out.print("checking " + url + "...");
            if (rs.getLastModified() < url.openConnection().getLastModified()) {
                updates.add(url);
                totalUpdates += 100;
                System.err.println("needs update!");
            } else {
                System.out.println("up-to-date");
            }
        }
        if (totalUpdates > 0) {
            if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null, "An update is available\nDownload now?", "Install Updates", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                DownloadService ds = (DownloadService) ServiceManager.lookup("javax.jnlp.DownloadService");
                System.out.println("applying updates:");
                try {
                    ds.loadResource(new URL("http", "localhost", "/webstart/lib/JMeldDiffView-1.0-SNAPSHOT.jar"), null, ds.getDefaultProgressWindow());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (URL url : updates) {
                    System.out.print("updating " + url + "...");
                    ds.removeResource(url, null);
                    ds.loadResource(url, null, ds.getDefaultProgressWindow());
                    System.out.println("done");
                    currentUpdate += 100;
                }
            } else {
                return -1;
            }
        }
        return updates.size();
    }
}
