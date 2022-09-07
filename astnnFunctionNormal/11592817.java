class BackupThread extends Thread {
    public IGenericChannelTemplate getChannelTemplate() throws XAwareException {
        String url = getProperty(XAwareConstants.BIZDRIVER_URL);
        URL sfURL = null;
        SoapBindingStub binding = null;
        if (url != null && url.length() > 0) {
            try {
                sfURL = new URL(url);
            } catch (java.net.MalformedURLException e) {
                throw new XAwareException("Malformed " + XAwareConstants.xaNamespace.getPrefix() + ":" + XAwareConstants.BIZDRIVER_URL + e.getLocalizedMessage());
            }
        }
        try {
            if (sfURL != null) {
                binding = (SoapBindingStub) new SforceServiceLocator().getSoap(sfURL);
            } else {
                binding = (SoapBindingStub) new SforceServiceLocator().getSoap();
            }
        } catch (ServiceException e) {
            throw new XAwareException("Service exception" + e.getLocalizedMessage());
        } catch (Exception e) {
            throw new XAwareException("Service exception" + e.getLocalizedMessage());
        }
        String timeout = getProperty(XAwareConstants.BIZDRIVER_TIMEOUT);
        int sfTimeout = 60000;
        if (timeout != null && !("".equals(timeout))) {
            try {
                sfTimeout = Integer.parseInt(timeout);
            } catch (NumberFormatException e) {
                throw new XAwareException("time out value must be a number: " + e.getLocalizedMessage());
            }
        }
        binding.setTimeout(sfTimeout);
        String uid = getProperty(XAwareConstants.BIZDRIVER_USER);
        if (uid == null) {
            uid = "";
        }
        String pwd = getProperty(XAwareConstants.BIZDRIVER_PWD);
        if (pwd == null) {
            pwd = "";
        }
        LoginResult loginResult;
        try {
            loginResult = binding.login(uid, pwd);
        } catch (LoginFault ex) {
            ExceptionCode exCode = ex.getExceptionCode();
            String message = null;
            if (exCode == ExceptionCode.FUNCTIONALITY_NOT_ENABLED) {
                message = "functionality not enabled.";
            } else if (exCode == ExceptionCode.INVALID_CLIENT) {
                message = "invalid client.";
            } else if (exCode == ExceptionCode.INVALID_LOGIN) {
                message = "invalid login.";
            } else if (exCode == ExceptionCode.LOGIN_DURING_RESTRICTED_DOMAIN) {
                message = "login during restricted domain.";
            } else if (exCode == ExceptionCode.LOGIN_DURING_RESTRICTED_TIME) {
                message = "login during restricted time.";
            } else if (exCode == ExceptionCode.ORG_LOCKED) {
                message = "org locked.";
            } else if (exCode == ExceptionCode.PASSWORD_LOCKOUT) {
                message = "password lockout.";
            } else if (exCode == ExceptionCode.SERVER_UNAVAILABLE) {
                message = "server unavailable.";
            } else if (exCode == ExceptionCode.TRIAL_EXPIRED) {
                message = "trial expired.";
            } else if (exCode == ExceptionCode.UNSUPPORTED_CLIENT) {
                message = "unsupported client.";
            } else {
                message = "an unexpected error.";
            }
            throw new XAwareException("Sales force returned " + message + ": " + ex.getExceptionMessage());
        } catch (Exception ex) {
            throw new XAwareException("An unexpected error has occurred: " + ex.getLocalizedMessage());
        }
        if (loginResult.isPasswordExpired()) {
            throw new XAwareException("An error has occurred. Your password has expired.");
        }
        binding._setProperty(SoapBindingStub.ENDPOINT_ADDRESS_PROPERTY, loginResult.getServerUrl());
        SessionHeader sh = new SessionHeader();
        sh.setSessionId(loginResult.getSessionId());
        binding.setHeader(new SforceServiceLocator().getServiceName().getNamespaceURI(), "SessionHeader", sh);
        return (IGenericChannelTemplate) new SalesForceTemplate(binding);
    }
}
