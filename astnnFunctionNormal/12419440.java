class BackupThread extends Thread {
    protected boolean wikiHasImage(String image) {
        try {
            URL url = new URL(URL + "index.php?title=Image:" + image);
            InputStream input = url.openStream();
            byte[] buffer = new byte[65536];
            int offset = 0;
            while (offset < buffer.length) {
                int count = input.read(buffer, offset, buffer.length - offset);
                if (count < 0) break;
                offset += count;
            }
            input.close();
            boolean hasFile = new String(buffer).indexOf("No file " + "by this name exists") < 0;
            if (hasFile) System.err.println("has image: " + new String(buffer));
            return hasFile;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
