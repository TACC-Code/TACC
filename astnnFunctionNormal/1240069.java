class BackupThread extends Thread {
    private boolean doVerify(PublicKey key, AttributeTable signedAttrTable, Provider sigProvider) throws CMSException, NoSuchAlgorithmException {
        String digestName = CMSSignedHelper.INSTANCE.getDigestAlgName(this.getDigestAlgOID());
        String signatureName = digestName + "with" + CMSSignedHelper.INSTANCE.getEncryptionAlgName(this.getEncryptionAlgOID());
        Signature sig = CMSSignedHelper.INSTANCE.getSignatureInstance(signatureName, sigProvider);
        MessageDigest digest = CMSSignedHelper.INSTANCE.getDigestInstance(digestName, sigProvider);
        try {
            sig.initVerify(key);
            if (signedAttributes == null) {
                if (content != null) {
                    content.write(new CMSSignedDataGenerator.SigOutputStream(sig));
                    content.write(new CMSSignedDataGenerator.DigOutputStream(digest));
                    resultDigest = digest.digest();
                } else {
                    resultDigest = digestCalculator.getDigest();
                    return verifyDigest(resultDigest, key, this.getSignature(), sigProvider);
                }
            } else {
                byte[] hash;
                if (content != null) {
                    content.write(new CMSSignedDataGenerator.DigOutputStream(digest));
                    hash = digest.digest();
                } else if (digestCalculator != null) {
                    hash = digestCalculator.getDigest();
                } else {
                    hash = null;
                }
                resultDigest = hash;
                Attribute dig = signedAttrTable.get(CMSAttributes.messageDigest);
                Attribute type = signedAttrTable.get(CMSAttributes.contentType);
                if (dig == null) {
                    throw new SignatureException("no hash for content found in signed attributes");
                }
                if (type == null && !contentType.equals(CMSAttributes.counterSignature)) {
                    throw new SignatureException("no content type id found in signed attributes");
                }
                DERObject hashObj = dig.getAttrValues().getObjectAt(0).getDERObject();
                if (hashObj instanceof ASN1OctetString) {
                    byte[] signedHash = ((ASN1OctetString) hashObj).getOctets();
                    if (!MessageDigest.isEqual(hash, signedHash)) {
                        throw new SignatureException("content hash found in signed attributes different");
                    }
                } else if (hashObj instanceof DERNull) {
                    if (hash != null) {
                        throw new SignatureException("NULL hash found in signed attributes when one expected");
                    }
                }
                if (type != null) {
                    DERObjectIdentifier typeOID = (DERObjectIdentifier) type.getAttrValues().getObjectAt(0);
                    if (!typeOID.equals(contentType)) {
                        throw new SignatureException("contentType in signed attributes different");
                    }
                }
                sig.update(this.getEncodedSignedAttributes());
            }
            return sig.verify(this.getSignature());
        } catch (InvalidKeyException e) {
            throw new CMSException("key not appropriate to signature in message.", e);
        } catch (IOException e) {
            throw new CMSException("can't process mime object to create signature.", e);
        } catch (SignatureException e) {
            throw new CMSException("invalid signature format in message: " + e.getMessage(), e);
        }
    }
}
