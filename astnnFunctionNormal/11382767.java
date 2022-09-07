class BackupThread extends Thread {
    @Test
    public void getInputStream() throws XPathException, IOException {
        BinaryValueManager binaryValueManager = new MockBinaryValueManager();
        try {
            final byte[] testData = "test data".getBytes();
            InputStream bais = new FilterInputStream(new ByteArrayInputStream(testData)) {

                @Override
                public boolean markSupported() {
                    return false;
                }
            };
            BinaryValue binaryValue = BinaryValueFromInputStream.getInstance(binaryValueManager, new Base64BinaryValueType(), bais);
            InputStream is = binaryValue.getInputStream();
            int read = -1;
            byte buf[] = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((read = is.read(buf)) > -1) {
                baos.write(buf, 0, read);
            }
            assertArrayEquals(testData, baos.toByteArray());
        } finally {
            binaryValueManager.cleanupBinaryValueInstances();
        }
    }
}
