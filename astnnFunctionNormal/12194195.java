class BackupThread extends Thread {
    @Test
    public void testHandleMessageWithAudit() throws Exception {
        KeyPair keyPair = MiscTestUtils.generateKeyPair();
        DateTime notBefore = new DateTime();
        DateTime notAfter = notBefore.plusYears(1);
        X509Certificate certificate = MiscTestUtils.generateCertificate(keyPair.getPublic(), "CN=Test,SERIALNUMBER=1234", notBefore, notAfter, null, keyPair.getPrivate(), true, 0, null, null);
        ServletConfig mockServletConfig = EasyMock.createMock(ServletConfig.class);
        Map<String, String> httpHeaders = new HashMap<String, String>();
        HttpSession mockHttpSession = EasyMock.createMock(HttpSession.class);
        HttpServletRequest mockServletRequest = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(mockServletConfig.getInitParameter("AuditService")).andStubReturn(null);
        EasyMock.expect(mockServletConfig.getInitParameter("AuditServiceClass")).andStubReturn(AuditTestService.class.getName());
        EasyMock.expect(mockServletConfig.getInitParameter("SignatureService")).andStubReturn(null);
        EasyMock.expect(mockServletConfig.getInitParameter("SignatureServiceClass")).andStubReturn(SignatureTestService.class.getName());
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte[] document = "hello world".getBytes();
        byte[] digestValue = messageDigest.digest(document);
        EasyMock.expect(mockHttpSession.getAttribute(SignatureDataMessageHandler.DIGEST_VALUE_SESSION_ATTRIBUTE)).andStubReturn(digestValue);
        EasyMock.expect(mockHttpSession.getAttribute(SignatureDataMessageHandler.DIGEST_ALGO_SESSION_ATTRIBUTE)).andStubReturn("SHA-1");
        SignatureDataMessage message = new SignatureDataMessage();
        message.certificateChain = new LinkedList<X509Certificate>();
        message.certificateChain.add(certificate);
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(keyPair.getPrivate());
        signature.update(document);
        byte[] signatureValue = signature.sign();
        message.signatureValue = signatureValue;
        EasyMock.replay(mockServletConfig, mockHttpSession, mockServletRequest);
        AppletServiceServlet.injectInitParams(mockServletConfig, this.testedInstance);
        this.testedInstance.init(mockServletConfig);
        this.testedInstance.handleMessage(message, httpHeaders, mockServletRequest, mockHttpSession);
        EasyMock.verify(mockServletConfig, mockHttpSession, mockServletRequest);
        assertEquals(signatureValue, SignatureTestService.getSignatureValue());
        assertEquals("1234", AuditTestService.getAuditSigningUserId());
    }
}
