class BackupThread extends Thread {
    protected void saveProjectFile(URI physicalURI) throws OWLOntologyStorageException {
        try {
            OutputStream os;
            if (!physicalURI.isAbsolute()) {
                throw new OWLOntologyStorageException("Physical URI must be absolute: " + physicalURI);
            }
            if (physicalURI.getScheme().equals("file")) {
                File file = new File(physicalURI);
                file.getParentFile().mkdirs();
                os = new FileOutputStream(file);
            } else {
                URL url = physicalURI.toURL();
                URLConnection conn = url.openConnection();
                os = conn.getOutputStream();
            }
            Writer w = new BufferedWriter(new OutputStreamWriter(os));
            RDFProjectRenderer renderer = new RDFProjectRenderer(this, model, w);
            renderer.render();
            w.close();
        } catch (IOException e) {
            throw new OWLOntologyStorageException(e);
        }
    }
}
