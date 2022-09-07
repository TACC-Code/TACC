class BackupThread extends Thread {
    public boolean verificarFirma(OMElement omRequest, MessageContext synCtx) {
        SynapseLog synLog = getLog(synCtx);
        boolean verificacionOK = false;
        OMElement node = null, response = null;
        String nodeName, nodeValue;
        String datos = "", firma = "", formatoFirma = "";
        String TAG_DATOS = "datos", TAG_FIRMA = "firma", TAG_FORMATO_FIRMA = "formatoFirma";
        String FORMATO_FIRMA_XADES_BES = "XADES-BES", FORMATO_FIRMA_XADES_T = "XADES-T", FORMATO_FIRMA_CADES_BES = "CADES-BES", FORMATO_FIRMA_CADES_T = "CADES-T";
        for (Iterator it = omRequest.getChildElements(); it.hasNext(); ) {
            synLog.traceOrDebug(" *** SI hay Childs");
            Object o = it.next();
            if (o instanceof OMElement) {
                node = (OMElement) o;
                nodeName = node.getLocalName();
                nodeValue = node.getText();
                if (nodeName.equals(TAG_DATOS)) {
                    datos = nodeValue;
                } else if (nodeName.equals(TAG_FIRMA)) {
                    firma = nodeValue;
                } else if (nodeName.equals(TAG_FORMATO_FIRMA)) {
                    formatoFirma = nodeValue;
                }
            }
        }
        HashMap<String, String> res = new HashMap<String, String>();
        res.put(TAG_DATOS, datos);
        res.put(TAG_FIRMA, firma);
        res.put(TAG_FORMATO_FIRMA, formatoFirma);
        synLog.traceOrDebug(" *** Datos: " + datos);
        synLog.traceOrDebug(" *** Firma: " + firma);
        synLog.traceOrDebug(" *** FormatoFirma: " + formatoFirma);
        byte[] firmaBytes = org.apache.commons.codec.binary.Base64.decodeBase64(firma.getBytes());
        byte[] documentoBytes = org.apache.commons.codec.binary.Base64.decodeBase64(datos.getBytes());
        byte[] datosEnFirma = null;
        try {
            if (formatoFirma.equals(FORMATO_FIRMA_CADES_BES) || formatoFirma.equals(FORMATO_FIRMA_CADES_T)) {
                synLog.traceOrDebug(" *** Es formato de firma CADES");
                synLog.traceOrDebug(" *** Es formato de firma CADES 1. FirmaBytes: " + firmaBytes.toString());
                synLog.traceOrDebug(" *** Es formato de firma CADES 2. documentoBytes: " + documentoBytes.toString());
                ByteArrayInputStream bis = new ByteArrayInputStream(firmaBytes);
                synLog.traceOrDebug(" *** Es formato de firma CADES 3 ");
                ASN1InputStream lObjDerOut = new ASN1InputStream(bis);
                DERObject lObjDER = null;
                synLog.traceOrDebug(" *** Es formato de firma CADES 4.lObjDerOut: " + lObjDerOut);
                try {
                    lObjDER = lObjDerOut.readObject();
                    synLog.traceOrDebug(" *** Es formato de firma CADES 5. lObjDER: " + lObjDER);
                } catch (IOException e) {
                    synLog.traceOrDebug("No encuentra formato de la firma");
                    handleException("No encuentra formato de la firma", synCtx);
                }
                synLog.traceOrDebug(" *** Es formato de firma CADES 6 ");
                ContentInfo lObjPKCS7 = ContentInfo.getInstance(lObjDER);
                synLog.traceOrDebug(" *** Es formato de firma CADES 7 ");
                SignedData lObjSignedData = SignedData.getInstance(lObjPKCS7.getContent());
                synLog.traceOrDebug(" *** Es formato de firma CADES 8 ");
                ContentInfo lObjContent = lObjSignedData.getContentInfo();
                synLog.traceOrDebug(" *** Es formato de firma CADES 9 ");
                datosEnFirma = ((ASN1OctetString) lObjContent.getContent()).getOctets();
                synLog.traceOrDebug(" *** Es formato de firma CADES 10 ");
                synLog.traceOrDebug("Son iguales? " + Arrays.equals(documentoBytes, datosEnFirma));
                if (Arrays.equals(documentoBytes, datosEnFirma)) {
                    verificacionOK = true;
                }
            } else if (formatoFirma.equals(FORMATO_FIRMA_XADES_BES) || formatoFirma.equals(FORMATO_FIRMA_XADES_T)) {
                synLog.traceOrDebug(" *** Es formato de firma XADES 1");
                InputStream in = new ByteArrayInputStream(firmaBytes);
                synLog.traceOrDebug(" *** Es formato de firma XADES 2");
                XMLStreamReader reader = StAXUtils.createXMLStreamReader(in);
                synLog.traceOrDebug(" *** Es formato de firma XADES 3");
                StAXOMBuilder builder = new StAXOMBuilder(reader);
                synLog.traceOrDebug(" *** Es formato de firma XADES 4");
                OMElement documentElement = builder.getDocumentElement();
                synLog.traceOrDebug(" *** Es formato de firma XADES 5");
                AXIOMXPath xpath = null;
                OMElement nodo = null;
                xpath = new AXIOMXPath("/AFIRMA/CONTENT");
                synLog.traceOrDebug(" *** Es formato de firma XADES 6");
                nodo = (OMElement) xpath.selectSingleNode(documentElement);
                synLog.traceOrDebug(" *** Es formato de firma XADES 7");
                String hashB64Xades = nodo.getText();
                synLog.traceOrDebug(" *** HASH B64 EN XADES: " + hashB64Xades);
                MessageDigest dig = MessageDigest.getInstance("SHA1");
                byte[] hash = dig.digest(documentoBytes);
                String hashB64Doc = new String(org.apache.commons.codec.binary.Base64.encodeBase64(hash));
                synLog.traceOrDebug(" *** HASH B64 DOC:      " + hashB64Doc);
                synLog.traceOrDebug(" *** IGUALES?: " + hashB64Doc.equals(hashB64Xades));
                if (hashB64Doc.equals(hashB64Xades)) {
                    verificacionOK = true;
                }
            } else {
                handleException("Formato de firma irreconocible ('" + formatoFirma + "')", synCtx);
            }
        } catch (Exception ex) {
            synLog.traceOrDebug(" %%% ValidarDocumentoFirmaMediatorCIM *** . Excepcion en verificarFirma: " + stackTraceToString(ex));
        }
        return verificacionOK;
    }
}
