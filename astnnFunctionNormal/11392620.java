class BackupThread extends Thread {
    @Test
    public void test_validate_album_getInfo() {
        try {
            SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            File schemaLocation = new File("tes.xsd");
            Schema schema = factory.newSchema(schemaLocation);
            Validator validator = schema.newValidator();
            URL url = new URL("http://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key=b25b959554ed76058ac220b7b2e0a026&artist=Cher&album=Believe");
            InputStream inputStream = url.openStream();
            Source source = new StreamSource(inputStream);
            validator.validate(source);
        } catch (IOException ex) {
            Logger.getLogger(GetInfo_Test.class.getName()).log(Level.SEVERE, null, ex);
            assertFalse("File not found", true);
        } catch (SAXException ex) {
            Logger.getLogger(GetInfo_Test.class.getName()).log(Level.SEVERE, null, ex);
            assertFalse("Schema did not validate", true);
        }
        assertTrue(true);
    }
}
