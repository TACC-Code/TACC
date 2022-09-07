class BackupThread extends Thread {
    @Test
    public void testDigestIsRight() {
        assertNotNull(digester);
        assertEquals("b5ua881ui4pzws3O03/p9ZIm4n0=", digester.digest("message").trim());
        assertEquals("2jmj7l5rSw0yVb/vlWAYkK/YBwk=", digester.digest("").trim());
    }
}
