class BackupThread extends Thread {
    public static X509Certificate generateCertificate(byte[] buf, int off, int len) throws IOException {
        int test = buf[off] + buf[len - 1] + buf[off + len - 1];
        try {
            int start = 0;
            int size = 0;
            byte[] hash = new byte[16];
            X509Certificate res = null;
            int publicKeyLen;
            int publicKeyPos;
            int modulusPos;
            int modulusLen;
            int exponentPos;
            int exponentLen;
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(buf, off, len);
            md.digest(hash, 0, hash.length);
            res = new X509Certificate();
            res.idx = 0;
            res.enc = new byte[len];
            System.arraycopy(buf, off, res.enc, 0, len);
            res.fp = new byte[hash.length];
            System.arraycopy(hash, 0, res.fp, 0, hash.length);
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_SECURITY, "-------- Begin Certificate -------");
            }
            res.getLen(SEQUENCE_TYPE);
            res.TBSStart = res.idx;
            size = res.getLen(SEQUENCE_TYPE);
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_SECURITY, "-------- Begin TBSCertificate -------");
            }
            int sigAlgIdx = res.idx + size;
            res.TBSLen = sigAlgIdx - res.TBSStart;
            if ((res.enc[res.idx] & 0xf0) == 0xa0) {
                res.idx++;
                if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                    Logging.report(Logging.INFORMATION, LogChannels.LC_SECURITY, "Version info: " + Utils.hexEncode(res.enc, (res.idx + 1), res.enc[res.idx]));
                }
                size = (res.enc[res.idx++] & 0xff);
                if (res.idx + size > res.enc.length) {
                    throw new IOException("Version info too long");
                }
                res.version = (byte) (res.enc[res.idx + (size - 1)] + 1);
                res.idx += size;
            } else {
                res.version = 1;
            }
            size = res.getLen(INTEGER_TYPE);
            res.serialNumber = Utils.hexEncode(res.enc, res.idx, size);
            res.idx += size;
            byte id = res.getAlg();
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_SECURITY, "Algorithm Id: " + id);
            }
            start = res.idx;
            size = res.getLen(SEQUENCE_TYPE);
            int end = res.idx + size;
            try {
                res.issuer = res.getName(end);
                if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                    Logging.report(Logging.INFORMATION, LogChannels.LC_SECURITY, "Issuer: " + res.issuer);
                }
            } catch (Exception e) {
                throw new IOException("Could not parse issuer name");
            }
            try {
                res.match(ValiditySeq);
                res.match(UTCSeq);
                res.from = getUTCTime(res.enc, res.idx);
                res.idx += UTC_LENGTH;
                res.match(UTCSeq);
                res.until = getUTCTime(res.enc, res.idx);
                res.idx += UTC_LENGTH;
            } catch (Exception e) {
                throw new IOException("Could not parse validity information" + "caught " + e);
            }
            start = res.idx;
            size = res.getLen(SEQUENCE_TYPE);
            end = res.idx + size;
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_SECURITY, "Subject: " + Utils.hexEncode(res.enc, start, size));
            }
            if (size != 0) {
                try {
                    res.subject = res.getName(end);
                    if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                        Logging.report(Logging.INFORMATION, LogChannels.LC_SECURITY, "Subject: " + res.subject);
                    }
                } catch (Exception e) {
                    throw new IOException("Could not parse subject name");
                }
            }
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_SECURITY, "SubjectPublicKeyInfo follows");
            }
            publicKeyLen = res.getLen(SEQUENCE_TYPE);
            publicKeyPos = res.idx;
            id = res.getAlg();
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_SECURITY, "Public Key Algorithm: " + id);
            }
            if (id != RSA_ENCRYPTION) {
                res.idx = publicKeyPos + publicKeyLen;
            }
            res.getLen(BITSTRING_TYPE);
            if (res.enc[res.idx++] != 0x00) {
                throw new IOException("Bitstring error while parsing public " + "key information");
            }
            res.getLen(SEQUENCE_TYPE);
            size = res.getLen(INTEGER_TYPE);
            if (res.enc[res.idx] == (byte) 0x00) {
                size--;
                res.idx++;
            }
            modulusPos = res.idx;
            modulusLen = size;
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_SECURITY, "Modulus:  " + Utils.hexEncode(res.enc, modulusPos, modulusLen));
            }
            res.idx += size;
            size = res.getLen(INTEGER_TYPE);
            if (res.enc[res.idx] == (byte) 0x00) {
                size--;
                res.idx++;
            }
            exponentPos = res.idx;
            exponentLen = size;
            res.pubKey = new RSAPublicKey(res.enc, modulusPos, modulusLen, res.enc, exponentPos, exponentLen);
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_SECURITY, "Exponent: " + Utils.hexEncode(res.enc, exponentPos, exponentLen));
            }
            res.idx += size;
            if (res.idx != sigAlgIdx) {
                if (res.version < 3) {
                    throw new IOException("Unexpected extensions in old version cert" + res.version);
                } else {
                    res.parseExtensions(sigAlgIdx);
                }
            }
            res.sigAlg = res.getAlg();
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_SECURITY, "Signature Algorithm: " + res.getSigAlgName());
            }
            md = null;
            if (res.sigAlg == MD2_RSA) {
                md = MessageDigest.getInstance("MD2");
            } else if (res.sigAlg == MD5_RSA) {
                md = MessageDigest.getInstance("MD5");
            } else if (res.sigAlg == SHA1_RSA) {
                md = MessageDigest.getInstance("SHA-1");
            }
            if (md != null) {
                res.TBSCertHash = new byte[md.getDigestLength()];
                md.update(buf, off + res.TBSStart, res.TBSLen);
                md.digest(res.TBSCertHash, 0, res.TBSCertHash.length);
            }
            size = res.getLen(BITSTRING_TYPE);
            if (res.enc[res.idx++] != 0x00) {
                throw new IOException("Bitstring error in signature parsing");
            }
            int sigLen = (((size - 1) + 7) >>> 3) << 3;
            res.signature = new byte[sigLen];
            System.arraycopy(res.enc, res.idx, res.signature, (sigLen - (size - 1)), (size - 1));
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_SECURITY, sigLen + "-byte signature: " + Utils.hexEncode(res.signature));
            }
            return res;
        } catch (IndexOutOfBoundsException e) {
            throw new IOException("Bad length detected in cert DER");
        } catch (GeneralSecurityException e) {
            throw new IOException(e.toString());
        }
    }
}
