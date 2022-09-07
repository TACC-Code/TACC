class BackupThread extends Thread {
    public void testFilterPTag() {
        StringReader reader = new StringReader(html);
        StringWriter writer = new StringWriter();
        try {
            HTMLParser.process(reader, writer, this, true);
            String buffer = new String(writer.toString());
            System.out.println(buffer);
            assertEquals(buffer, "<html><head><title>test</title></head><body></body></html>");
        } catch (HandlingException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
