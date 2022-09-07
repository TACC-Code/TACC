class BackupThread extends Thread {
            public void actionPerformed(ActionEvent pE) {
                ScanAction action = new ScanAction("scan.append");
                action.scan(pE, (ScanSource) getScanSourceListBox().getSelectedItem());
                ArrayList<File> pages = action.getScannedPages();
                File scandir = new File(Configuration.getTmpDir() + File.separator + "scanned");
                scandir.mkdir();
                for (File f : pages) {
                    Documentpages p = new Documentpages();
                    File dest = new File(Configuration.getTmpDir() + File.separator + "scanned" + File.separator + f.getName());
                    try {
                        FileUtils.copyFile(f.getAbsoluteFile(), dest);
                        p.setFilename(dest.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                        p.setFilename(f.getAbsolutePath());
                    }
                    mPagesmodel.appendPage(p);
                }
                refreshPageNumbers();
            }
}
