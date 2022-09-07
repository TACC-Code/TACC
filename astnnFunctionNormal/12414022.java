class BackupThread extends Thread {
    @Override
    public MailUtils getSystemMailUtils() {
        if (systemMailUtils == null) {
            try {
                InputStream inStream = getClass().getResourceAsStream("/mail-server/mail_config.xml");
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                int size = 2048;
                byte[] buffer = new byte[size];
                int read;
                while ((read = inStream.read(buffer)) > 0) {
                    outStream.write(buffer, 0, read);
                }
                inStream.close();
                inStream = new ByteArrayInputStream(outStream.toByteArray());
                outStream.close();
                XMLDecoder decoder = new XMLDecoder(inStream);
                MailServer outgoingServer = (MailServer) decoder.readObject();
                decoder.close();
                outStream = new ByteArrayOutputStream();
                inStream = getClass().getResourceAsStream("/mail-server/from_address.xml");
                while ((read = inStream.read(buffer)) > 0) {
                    outStream.write(buffer, 0, read);
                }
                inStream.close();
                inStream = new ByteArrayInputStream(outStream.toByteArray());
                outStream.close();
                decoder = new XMLDecoder(inStream);
                InternetAddress fromAddress = (InternetAddress) decoder.readObject();
                decoder.close();
                systemMailUtils = MailUtils.getInstance(outgoingServer, fromAddress);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return systemMailUtils;
    }
}
