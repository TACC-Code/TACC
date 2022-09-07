class BackupThread extends Thread {
    protected InnerZipStreamDataNode(String path, ZipInputStream zin, IArchiveEntry parent, NodeFactory fac) throws IOException {
        super(fac);
        pathz = path.split("/");
        this.zin = zin;
        List<IArchiveEntry> zeList = new LinkedList<IArchiveEntry>();
        this.setName(pathz[pathz.length - 1]);
        this.setParent(parent);
        AdjustHelper adjustor = new AdjustHelper(this);
        ZipEntry en = null;
        byte[] buff = new byte[1024 * 256];
        while ((en = zin.getNextEntry()) != null) {
            ByteArrayOutputStream dataBank = new ByteArrayOutputStream();
            int offset = 0;
            while ((offset = zin.read(buff)) != -1) dataBank.write(buff, 0, offset);
            final InputStream in = new ByteArrayInputStream(dataBank.toByteArray());
            dataBank.close();
            InnerZipEntryDataNode node = new InnerZipEntryDataNode(en.getName(), en.isDirectory() ? null : in, parent, fac);
            adjustor.add(node);
        }
        adjustor.adjustNodeLocation();
    }
}
