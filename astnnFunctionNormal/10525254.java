class BackupThread extends Thread {
    public void verify(Manifest mf) throws IOException, NoSuchAlgorithmException {
        InputStream in;
        Attributes attributes;
        Map.Entry entry;
        Digester digester;
        String[] algs;
        Iterator i;
        String name;
        String s;
        byte[] b;
        Map digests;
        int n;
        if (mf == null) {
            throw new NullPointerException("Manifest");
        }
        digests = new HashMap();
        digester = new Digester(mf.trusted_, mf.threshold_);
        garbage_.removeAll(mf.entries_.keySet());
        for (i = mf.entries_.entrySet().iterator(); i.hasNext(); ) {
            entry = (Map.Entry) i.next();
            attributes = (Attributes) entry.getValue();
            name = (String) entry.getKey();
            in = source_.getInputStream(name);
            if (in == null) {
                missing_.add(name);
                continue;
            }
            algs = Digester.parseAlgorithms(attributes.getDigestAlgorithms());
            try {
                digester.digest(algs, digests, in);
                for (n = algs.length - 1; n >= 0; n--) {
                    if (!digests.containsKey(algs[n])) {
                        continue;
                    }
                    s = attributes.get(algs[n] + "-Digest");
                    if (s == null) {
                        failed_.add(name);
                        break;
                    }
                    b = (byte[]) digests.get(algs[n]);
                    if (!Arrays.equals(b, Base64.decode(s))) {
                        failed_.add(name);
                        break;
                    }
                }
            } catch (DigestException e) {
                failed_.add(name);
            } catch (CorruptedCodeException e) {
                failed_.add(name);
            } finally {
                in.close();
            }
        }
    }
}
