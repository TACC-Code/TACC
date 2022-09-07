class BackupThread extends Thread {
    private void update() throws java.io.IOException {
        this.allowPut = true;
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(this.contents));
        ZipEntry ze = null;
        java.io.ByteArrayOutputStream bao = null;
        byte[] buff = null;
        ;
        int i = 0;
        while ((ze = zis.getNextEntry()) != null) {
            String n = ze.getName();
            debug("[" + (i++) + "] " + n);
            if (sun.tools.jar.Manifest.isManifestName(n)) {
                this.manifest = new Manifest(zis);
            } else if (n.toUpperCase().startsWith("MANIFEST/") && n.toUpperCase().endsWith(".SF")) {
            } else {
                byte b[];
                long l = ze.getSize();
                if (l < 0) {
                    int read;
                    if (bao == null) {
                        buff = new byte[512];
                        bao = new java.io.ByteArrayOutputStream();
                    }
                    bao.reset();
                    while ((read = zis.read(buff, 0, 512)) > 0) {
                        bao.write(buff, 0, read);
                    }
                    b = bao.toByteArray();
                } else {
                    b = new byte[(int) l];
                    this.readFully(zis, b);
                }
                this.putResource(ze.getName(), b);
            }
        }
        this.allowPut = false;
    }
}
