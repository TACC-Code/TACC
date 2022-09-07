class BackupThread extends Thread {
    public void test_getHeaderFields() throws Exception {
        URL url = new URL(jettyURL);
        HttpURLConnection httpURLConnect = (HttpURLConnection) url.openConnection();
        assertEquals(200, httpURLConnect.getResponseCode());
        assertEquals("OK", httpURLConnect.getResponseMessage());
        Map headers = httpURLConnect.getHeaderFields();
        assertTrue(headers.size() > 1);
        List list = (List) headers.get("Content-Length");
        if (list == null) {
            list = (List) headers.get("content-length");
        }
        assertNotNull(list);
        try {
            headers.put("key", "value");
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
        try {
            list.set(0, "value");
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
        try {
            httpURLConnect.setRequestProperty("key", "value");
            fail("should throw IlegalStateException");
        } catch (IllegalStateException e) {
        }
    }
}
