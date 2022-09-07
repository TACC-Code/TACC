class BackupThread extends Thread {
    public XmlObject parse(InputStream jiois, SchemaType type, XmlOptions options) throws XmlException, IOException {
        XmlFactoryHook hook = XmlFactoryHook.ThreadContext.getHook();
        DigestInputStream digestStream = null;
        setupDigest: if (options != null && options.hasOption(XmlOptions.LOAD_MESSAGE_DIGEST)) {
            MessageDigest sha;
            try {
                sha = MessageDigest.getInstance("SHA");
            } catch (NoSuchAlgorithmException e) {
                break setupDigest;
            }
            digestStream = new DigestInputStream(jiois, sha);
            jiois = digestStream;
        }
        if (hook != null) return hook.parse(this, jiois, type, options);
        XmlObject result = Locale.parseToXmlObject(this, jiois, type, options);
        if (digestStream != null) result.documentProperties().setMessageDigest(digestStream.getMessageDigest().digest());
        return result;
    }
}
