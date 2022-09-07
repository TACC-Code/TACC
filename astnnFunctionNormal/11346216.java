class BackupThread extends Thread {
    protected void loadImage() throws AWTException {
        Image theImage = null;
        InputStream imageInputStream = getClass().getResourceAsStream(resourceName);
        if (imageInputStream == null) {
            loadFailed = true;
            throw new AWTException("Image resource " + resourceName + " not found.");
        }
        ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
        int arraySize = 1024;
        byte[] imageArray = new byte[arraySize];
        int readCode;
        if (imageInputStream == null) return;
        try {
            while ((readCode = imageInputStream.read(imageArray, 0, arraySize)) > -1) {
                imageBytes.write(imageArray, 0, readCode);
            }
            imageBytes.close();
            imageArray = imageBytes.toByteArray();
            imageInputStream.close();
            theImage = Toolkit.getDefaultToolkit().createImage(imageArray);
        } catch (IOException ioException) {
            System.out.println("Error reading image data");
            ioException.printStackTrace();
        }
        setImage(theImage);
        if (loadLater) return;
        waitForImage();
    }
}
