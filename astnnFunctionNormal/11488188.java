class BackupThread extends Thread {
    public static Map<String, byte[]> verify(InputStream in, String tarname, byte[] buf, ArrayList<String> objectNames) throws IOException, VerifyTarException {
        TarInputStream tar = new TarInputStream(in);
        try {
            log.debug("Verify tar file: {}", tarname);
            TarEntry entry = tar.getNextEntry();
            if (entry == null) throw new VerifyTarException("No entries in " + tarname);
            String entryName = entry.getName();
            if (!"MD5SUM".equals(entryName)) throw new VerifyTarException("Missing MD5SUM entry in " + tarname);
            BufferedReader dis = new BufferedReader(new InputStreamReader(tar));
            HashMap<String, byte[]> md5sums = new HashMap<String, byte[]>();
            String line;
            while ((line = dis.readLine()) != null) {
                char[] c = line.toCharArray();
                byte[] md5sum = new byte[16];
                for (int i = 0, j = 0; i < md5sum.length; i++, j++, j++) {
                    md5sum[i] = (byte) ((fromHexDigit(c[j]) << 4) | fromHexDigit(c[j + 1]));
                }
                md5sums.put(line.substring(34), md5sum);
            }
            Map<String, byte[]> entries = new HashMap<String, byte[]>(md5sums.size());
            entries.putAll(md5sums);
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            while ((entry = tar.getNextEntry()) != null) {
                entryName = entry.getName();
                log.debug("START: Check MD5 of entry: {}", entryName);
                if (objectNames != null && !objectNames.remove(entryName)) throw new VerifyTarException("TAR " + tarname + " contains entry: " + entryName + " not in file list");
                byte[] md5sum = (byte[]) md5sums.remove(entryName);
                if (md5sum == null) throw new VerifyTarException("Unexpected TAR entry: " + entryName + " in " + tarname);
                digest.reset();
                in = new DigestInputStream(tar, digest);
                while (in.read(buf) > 0) ;
                if (!Arrays.equals(digest.digest(), md5sum)) {
                    throw new VerifyTarException("Failed MD5 check of TAR entry: " + entryName + " in " + tarname);
                }
                log.debug("DONE: Check MD5 of entry: {}", entryName);
            }
            if (!md5sums.isEmpty()) throw new VerifyTarException("Missing TAR entries: " + md5sums.keySet() + " in " + tarname);
            if (objectNames != null && !objectNames.isEmpty()) throw new VerifyTarException("Missing TAR entries from object list: " + objectNames.toString() + " in " + tarname);
            return entries;
        } finally {
            tar.close();
        }
    }
}
