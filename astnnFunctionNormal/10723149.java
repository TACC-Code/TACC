class BackupThread extends Thread {
    public PasswordParameter(Composite composite, final String name, final int encoding) {
        super(name);
        this.name = name;
        inputField = new Text(composite, SWT.BORDER);
        inputField.setEchoChar('*');
        byte[] value = COConfigurationManager.getByteParameter(name, "".getBytes());
        if (value.length > 0) inputField.setText("***");
        inputField.addListener(SWT.Modify, new Listener() {

            public void handleEvent(Event event) {
                try {
                    String password_string = inputField.getText();
                    byte[] password = password_string.getBytes();
                    byte[] encoded;
                    if (password.length > 0) {
                        if (encoding == org.gudy.azureus2.plugins.ui.config.PasswordParameter.ET_PLAIN) {
                            encoded = password;
                        } else if (encoding == org.gudy.azureus2.plugins.ui.config.PasswordParameter.ET_SHA1) {
                            SHA1Hasher hasher = new SHA1Hasher();
                            encoded = hasher.calculateHash(password);
                        } else {
                            encoded = MessageDigest.getInstance("md5").digest(password_string.getBytes("UTF-8"));
                        }
                    } else {
                        encoded = password;
                    }
                    COConfigurationManager.setParameter(name, encoded);
                } catch (Exception e) {
                    Debug.printStackTrace(e);
                }
            }
        });
    }
}
