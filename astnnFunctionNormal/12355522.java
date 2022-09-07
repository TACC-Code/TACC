class BackupThread extends Thread {
    protected synchronized void initialize(URI carmlURI, Map<String, Object> properties) throws FileNotFoundException, IDBeanException, IllegalAccessException {
        String appCredStr = null;
        Subject appCredential = null;
        if (System.getProperty(ArisIdConstants.BASE64_CLASS) == null) {
            System.setProperty(ArisIdConstants.BASE64_CLASS, ArisIdConstants.DEFAULT_BASE64_CLASS);
        }
        if (properties != null) {
            String user = (String) properties.get(ArisIdConstants.SECURITY_PRINCIPAL);
            Object p = properties.get(ArisIdConstants.SECURITY_CREDENTIALS);
            byte[] pwd = null;
            if (p != null) {
                if (p instanceof byte[]) pwd = (byte[]) p; else {
                    try {
                        pwd = ((String) p).getBytes("UTF-8");
                    } catch (java.io.UnsupportedEncodingException uee) {
                        pwd = ((String) p).getBytes();
                    }
                }
            }
            if (user != null & pwd != null) {
                appCredential = new Subject();
                appCredential.getPrincipals().add(new PrincipalIdentifier(user));
                appCredential.getPrivateCredentials().add(pwd);
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(pwd);
                    byte raw[] = md.digest();
                    String encPwd = AttributeValue.base64Encode(raw);
                    appCredStr = user + encPwd;
                } catch (NoSuchAlgorithmException e) {
                    throw new IDBeanException(e);
                }
            }
            if (properties.get(ArisIdConstants.ATTRIBUTE_SERVICE_PROVIDER) != null) {
                System.setProperty(ArisIdConstants.ATTRIBUTE_SERVICE_PROVIDER, (String) properties.get(ArisIdConstants.ATTRIBUTE_SERVICE_PROVIDER));
            }
        }
        if (System.getProperty(ArisIdConstants.ATTRIBUTE_SERVICE_PROVIDER) == null) {
            System.setProperty(ArisIdConstants.ATTRIBUTE_SERVICE_PROVIDER, ArisIdConstants.DEFAULT_ATTRIBUTE_SERVICE_PROVIDER);
        }
        if (System.getProperty(ArisIdConstants.WS_POLICY_CLASS) == null) {
            System.setProperty(ArisIdConstants.WS_POLICY_CLASS, ArisIdConstants.DEFAULT_WS_POLICY_CLASS);
        }
        if (System.getProperty(DOMImplementationRegistry.PROPERTY) == null) System.setProperty(DOMImplementationRegistry.PROPERTY, ArisIdConstants.DEFAULT_DOMIMPLEMENTATIONSOURCE);
        try {
            String key = carmlURI.toString() + appCredStr;
            if (asvcMap.containsKey(key)) {
                logr.debug("Getting ArisId service from map for CARML file: " + carmlURI);
                asvc = asvcMap.get(key);
                Integer refCnt = asvcRefCntMap.get(asvc);
                asvcRefCntMap.put(asvc, ++refCnt);
                logr.trace("Incremented ArisId reference count to " + refCnt);
            } else {
                logr.debug("Creating ArisId service for CARML file: " + carmlURI);
                asvc = ArisIdServiceFactory.createAttributeService(appCredential, carmlURI);
                asvcMap.put(key, asvc);
                asvcRefCntMap.put(asvc, 1);
                logr.trace("Set ArisId reference count to 1");
                logr.debug("ArisId service created for CARML file: " + carmlURI);
            }
        } catch (AuthenticationException e) {
            logr.debug(e.toString());
            throw new IDBeanException("Authentication Failed", e);
        } catch (NoSuchContextException e) {
            logr.debug(e.toString());
            throw new IDBeanException(e);
        } catch (NoSuchSubjectException e) {
            logr.debug(e.toString());
            throw new IDBeanException(e);
        } catch (SubjectNotUniqueException e) {
            logr.debug(e.toString());
            throw new IDBeanException(e);
        } catch (ClassNotFoundException e) {
            logr.debug(e.toString());
            throw new IDBeanException(e);
        } catch (InstantiationException e) {
            logr.debug(e.toString());
            throw new IDBeanException(e);
        } catch (IGFException e) {
            logr.debug(e.toString());
            throw new IDBeanException(e);
        } finally {
            appCredential = null;
        }
        polHandler = PolicyHandler.getInstance();
    }
}
