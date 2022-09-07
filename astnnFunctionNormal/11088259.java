class BackupThread extends Thread {
    protected void deliverSpecial() throws IOException {
        requestedFile = requestedFile.substring(2);
        if (requestedFile.equalsIgnoreCase("index.html")) {
            deliverMain();
            return;
        } else if (requestedFile.equalsIgnoreCase("tree.html")) {
            deliverTree();
            return;
        } else if (requestedFile.equalsIgnoreCase("search.html")) {
            deliverSearch();
            return;
        } else if (requestedFile.equalsIgnoreCase("search2.html")) {
            deliverSearch2();
            return;
        }
        File f = new File(templatePath + "/template/" + requestedFile);
        if (!f.canRead()) {
            response.sendHeader("text/plain");
            response.sendString("404: not found: " + f.getAbsolutePath());
            return;
        }
        response.sendHeader(request.getContentType());
        RandomAccessFile rf = new RandomAccessFile(templatePath + "/template/" + requestedFile, "r");
        ByteBuffer in = rf.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, rf.length());
        response.write(in, (int) rf.length());
    }
}
