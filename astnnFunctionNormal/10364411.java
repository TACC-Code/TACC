class BackupThread extends Thread {
    public static byte[] downloadGravatar(final String email, int size) throws EnMeGenericException {
        InputStream stream = null;
        try {
            URL url = new URL(getUrl(email, size));
            stream = url.openStream();
            return IOUtils.toByteArray(stream);
        } catch (FileNotFoundException e) {
            return null;
        } catch (Exception e) {
            throw new EnMeGenericException(e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }
}
