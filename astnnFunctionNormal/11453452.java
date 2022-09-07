class BackupThread extends Thread {
    public FileDownloader(String fileURL) throws Exception {
        URL url;
        try {
            url = new URL(fileURL);
        } catch (MalformedURLException e) {
            System.out.println("Error accessing URL " + fileURL + ".");
            throw e;
        }
        InputStream is;
        try {
            is = url.openStream();
        } catch (IOException e) {
            System.out.println("Error creating Input Stream from URL '" + fileURL + "'.");
            throw e;
        }
        DataInputStream dis = new DataInputStream(new BufferedInputStream(is));
        try {
            tempFile = File.createTempFile("tempz", ".tmpz");
        } catch (IOException e) {
            System.out.println("Error creating temp file. Check space available.");
            throw e;
        }
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(tempFile);
        } catch (FileNotFoundException e) {
            System.out.println("Error creating temp file. Check permissions.");
            throw e;
        }
        try {
            int numBytesRead = 0;
            while ((numBytesRead = dis.read(buffer)) != -1) {
                fos.write(buffer, 0, numBytesRead);
            }
            fos.flush();
            fos.close();
        } catch (IOException e) {
            System.out.println("Error reading from URL " + fileURL + ".");
            throw e;
        }
    }
}
