class BackupThread extends Thread {
    public SignerInformationStore getSignerInfos() throws CMSException {
        if (_signerInfoStore == null) {
            populateCertCrlSets();
            List signerInfos = new ArrayList();
            Map hashes = new HashMap();
            Iterator it = _digests.keySet().iterator();
            while (it.hasNext()) {
                Object digestKey = it.next();
                hashes.put(digestKey, ((MessageDigest) _digests.get(digestKey)).digest());
            }
            try {
                ASN1SetParser s = _signedData.getSignerInfos();
                DEREncodable o;
                while ((o = s.readObject()) != null) {
                    SignerInfo info = SignerInfo.getInstance(o.getDERObject());
                    String digestName = HELPER.getDigestAlgName(info.getDigestAlgorithm().getObjectId().getId());
                    byte[] hash = (byte[]) hashes.get(digestName);
                    signerInfos.add(new SignerInformation(info, _signedContentType, null, new BaseDigestCalculator(hash)));
                }
            } catch (IOException e) {
                throw new CMSException("io exception: " + e.getMessage(), e);
            }
            _signerInfoStore = new SignerInformationStore(signerInfos);
        }
        return _signerInfoStore;
    }
}
