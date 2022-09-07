class BackupThread extends Thread {
    public void testSetCookieVersionMix() throws Exception {
        this.localServer.register("*", new SetCookieVersionMixService());
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);
        CookieStore cookieStore = new BasicCookieStore();
        HttpContext context = new BasicHttpContext();
        context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        HttpGet httpget = new HttpGet("/test/");
        HttpResponse response1 = client.execute(getServerHttp(), httpget, context);
        HttpEntity e1 = response1.getEntity();
        if (e1 != null) {
            e1.consumeContent();
        }
        List<Cookie> cookies = cookieStore.getCookies();
        assertNotNull(cookies);
        assertEquals(1, cookies.size());
        assertEquals("right", cookies.get(0).getValue());
        assertTrue(cookies.get(0) instanceof SetCookie2);
    }
}
