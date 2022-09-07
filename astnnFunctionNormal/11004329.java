class BackupThread extends Thread {
    private void _createBackendCatalog(BundleContext argContext) throws IOException {
        File targetDir = argContext.getDataFile("/slinksrv/");
        targetDir.mkdirs();
        _backendCatalogPath = targetDir.getAbsolutePath();
        File targetFile = new File(targetDir, "eslinksrv.cpo");
        Bundle bundle = argContext.getBundle();
        URL url = bundle.getEntry("/sasbin/slinksrv/eslinksrv.cpo");
        InputStream in = new BufferedInputStream(url.openStream());
        OutputStream out = new BufferedOutputStream(new FileOutputStream(targetFile));
        int c;
        while ((c = in.read()) != -1) out.write(c);
        out.close();
        in.close();
    }
}
