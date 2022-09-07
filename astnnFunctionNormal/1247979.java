class BackupThread extends Thread {
    private String loadSegmentCache(EventSource source) throws IOException {
        String eventPath = cvtLogDir + File.separator + source.type + "_" + source.name;
        FileInputStream in = new FileInputStream(eventPath + ".xml");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[2048];
        int read = in.read(buf);
        while (read != -1) {
            out.write(buf, 0, read);
            read = in.read(buf);
        }
        in.close();
        String segment = out.toString();
        Integer[] buckets = new Integer[24];
        try {
            ObjectInputStream pin = new ObjectInputStream(new FileInputStream(eventPath + ".prof"));
            for (int i = 0; i < 24; i++) {
                buckets[i] = (Integer) pin.readObject();
            }
            pin.close();
        } catch (ClassNotFoundException cnfe) {
            ;
        }
        segmentCache.addSegment(source, segment, buckets);
        return segment;
    }
}
