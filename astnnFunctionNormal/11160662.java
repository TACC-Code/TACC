class BackupThread extends Thread {
    public void writeToFile(String url, String filePath) throws IOException {
        URL imageUrl = new URL(url);
        System.out.println("Requesting to: " + url);
        URLConnection urlC = imageUrl.openConnection();
        System.out.print("Copying resource (type: " + urlC.getContentType());
        Date date = new Date(urlC.getLastModified());
        InputStream is = imageUrl.openStream();
        FileOutputStream fos = new FileOutputStream(filePath);
        int oneChar, count = 0;
        while ((oneChar = is.read()) != -1) {
            fos.write(oneChar);
            count++;
        }
        is.close();
        fos.close();
    }
}
