class BackupThread extends Thread {
    private Element getReference() throws SerializationException {
        Element reference = new Element("dsig:Reference", "http://www.w3.org/2000/09/xmldsig#");
        Attribute uriAttr = null;
        if (id.equals("")) {
            uriAttr = new Attribute("URI", "");
        } else {
            uriAttr = new Attribute("URI", "#" + id);
        }
        reference.addAttribute(uriAttr);
        Element transforms = new Element("dsig:Transforms", "http://www.w3.org/2000/09/xmldsig#");
        if (enveloped) {
            Element transformEnveloped = new Element("dsig:Transform", "http://www.w3.org/2000/09/xmldsig#");
            Attribute transformEnvelopedAlgorithm = new Attribute("Algorithm", "http://www.w3.org/2000/09/xmldsig#enveloped-signature");
            transformEnveloped.addAttribute(transformEnvelopedAlgorithm);
            transforms.appendChild(transformEnveloped);
        }
        Element transformDsig = new Element("dsig:Transform", "http://www.w3.org/2000/09/xmldsig#");
        String method = Canonicalizer.EXCLUSIVE_XML_CANONICALIZATION;
        Attribute transformDsigAlgorithm = new Attribute("Algorithm", method);
        transformDsig.addAttribute(transformDsigAlgorithm);
        if (inclusiveNamespacePrefixes != null) {
            Element inclusiveNamespacePrefixesE = new Element("ec:InclusiveNamespaces", "http://www.w3.org/2001/10/xml-exc-c14n#");
            Attribute prefixList = new Attribute("PrefixList", inclusiveNamespacePrefixes);
            inclusiveNamespacePrefixesE.addAttribute(prefixList);
            transformDsig.appendChild(inclusiveNamespacePrefixesE);
        }
        transforms.appendChild(transformDsig);
        reference.appendChild(transforms);
        Element digestMethod = new Element("dsig:DigestMethod", "http://www.w3.org/2000/09/xmldsig#");
        String algorithmName = CryptoUtils.convertMessageDigestAlgorithm(digestAlgorithmName);
        Attribute digestAlgorithm = new Attribute("Algorithm", algorithmName);
        digestMethod.addAttribute(digestAlgorithm);
        reference.appendChild(digestMethod);
        Element digestValue = new Element("dsig:DigestValue", "http://www.w3.org/2000/09/xmldsig#");
        byte[] dataBytes = null;
        try {
            dataBytes = XmlUtils.canonicalize(data, method);
        } catch (IOException e) {
            throw new SerializationException("Error canonicalizing data to be digested", e);
        }
        try {
            String digest = CryptoUtils.digest(dataBytes, digestAlgorithmName);
            digestValue.appendChild(digest);
        } catch (org.xmldap.exceptions.CryptoException e) {
            throw new SerializationException("Error digesting canonicalized data", e);
        }
        reference.appendChild(digestValue);
        return reference;
    }
}
