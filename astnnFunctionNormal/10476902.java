class BackupThread extends Thread {
    private byte[] getImage(Image image) throws DocumentException {
        ByteArrayOutputStream imageTemp = new ByteArrayOutputStream();
        try {
            InputStream imageIn;
            if (imageType == Image.ORIGINAL_BMP) {
                imageIn = new ByteArrayInputStream(MetaDo.wrapBMP(image));
            } else {
                if (image.getOriginalData() == null) {
                    imageIn = image.url().openStream();
                } else {
                    imageIn = new ByteArrayInputStream(image.getOriginalData());
                }
                if (imageType == Image.ORIGINAL_WMF) {
                    long skipLength = 22;
                    while (skipLength > 0) {
                        skipLength = skipLength - imageIn.skip(skipLength);
                    }
                }
            }
            int buffer = 0;
            int count = 0;
            while ((buffer = imageIn.read()) != -1) {
                String helperStr = Integer.toHexString(buffer);
                if (helperStr.length() < 2) helperStr = "0" + helperStr;
                imageTemp.write(helperStr.getBytes());
                count++;
                if (count == 64) {
                    imageTemp.write((byte) '\n');
                    count = 0;
                }
            }
        } catch (IOException ioe) {
            throw new DocumentException(ioe.getMessage());
        }
        return imageTemp.toByteArray();
    }
}
