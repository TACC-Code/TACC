class BackupThread extends Thread {
    public static String[] getStrings(final String username, final String password, final String challenge) throws NoSuchAlgorithmException, IOException {
        int operand = 0, i;
        int cnt = 0;
        for (i = 0; i < challenge.length(); i++) {
            if (isOperator(challenge.charAt(i))) {
                cnt++;
            }
        }
        int[] magic = new int[cnt];
        cnt = 0;
        for (i = 0; i < challenge.length(); i++) {
            char c = challenge.charAt(i);
            if (Character.isLetter(c) || Character.isDigit(c)) {
                operand = ALPHANUM_LOOKUP.indexOf(c) << 3;
            } else if (isOperator(c)) {
                int a = OPERATORS_LOOKUP.indexOf(c);
                magic[cnt] = (operand | a) & 0xff;
                cnt++;
            }
        }
        for (i = magic.length - 2; i >= 0; i--) {
            int a = magic[i];
            int b = magic[i + 1];
            a = ((a * 0xcd) ^ b) & 0xff;
            magic[i + 1] = a;
        }
        byte[] comparison = _part3Munge(magic);
        long seed = 0;
        byte[] binLookup = new byte[7];
        for (i = 0; i < 4; i++) {
            seed = seed << 8;
            seed += (comparison[3 - i] & 0xff);
            binLookup[i] = (byte) (comparison[i] & 0xff);
        }
        int table = 0, depth = 0;
        MessageDigest localMd5 = MessageDigest.getInstance("MD5");
        for (i = 0; i < 0xffff; i++) {
            for (int j = 0; j < 5; j++) {
                binLookup[4] = (byte) (i & 0xff);
                binLookup[5] = (byte) ((i >> 8) & 0xff);
                binLookup[6] = (byte) j;
                localMd5.reset();
                byte[] result = localMd5.digest(binLookup);
                if (_part3Compare(result, comparison) == true) {
                    depth = i;
                    table = j;
                    i = 0xffff;
                    j = 5;
                }
            }
        }
        byte[] magicValue = new byte[4];
        seed = _part3Lookup(table, depth, seed);
        seed = _part3Lookup(table, depth, seed);
        for (i = 0; i < magicValue.length; i++) {
            magicValue[i] = (byte) (seed & 0xff);
            seed = seed >> 8;
        }
        String regular = yahoo64(md5(password));
        String crypted = yahoo64(md5(md5Crypt(password, "$1$_2S43d5f")));
        boolean hackSha1 = (table >= 3);
        String[] s = new String[2];
        s[0] = _part4Encode(_part4Hash(regular, magicValue, hackSha1));
        s[1] = _part4Encode(_part4Hash(crypted, magicValue, hackSha1));
        return s;
    }
}
