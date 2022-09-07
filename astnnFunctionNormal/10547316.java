class BackupThread extends Thread {
    private String createHeader(HttpURLConnection urlConnection) {
        MessageDigest messageDigest = null;
        StringBuffer buffer = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            StringBuilder sb = new StringBuilder(256);
            Formatter formatter = new Formatter(sb, Locale.US);
            formatter.format("%08x", nonceCount);
            nc = sb.toString();
            if (charset == null) {
                charset = NetworkHelper.DEFAULT_ELEMENT_CHARSET;
            }
            String cnonce = createCnonce();
            buffer = new StringBuffer();
            buffer.append(username).append(":").append(realm).append(":").append(password);
            String hash1 = buffer.toString();
            hash1 = encode(messageDigest.digest(hash1.getBytes(charset)));
            buffer = new StringBuffer();
            buffer.append(urlConnection.getRequestMethod()).append(":").append(urlConnection.getURL().getPath());
            String hash2 = buffer.toString();
            hash2 = encode(messageDigest.digest(hash2.getBytes(charset)));
            buffer = new StringBuffer();
            buffer.append(hash1).append(":").append(nonce).append(":").append(nc).append(":").append(cnonce).append(":").append(qop).append(":").append(hash2);
            String response = buffer.toString();
            response = encode(messageDigest.digest(response.getBytes(charset)));
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new NameValuePair("username", username));
            params.add(new NameValuePair("realm", realm));
            params.add(new NameValuePair("nonce", nonce));
            params.add(new NameValuePair("uri", urlConnection.getURL().getPath()));
            params.add(new NameValuePair("algorithm", algorithm));
            params.add(new NameValuePair("response", response));
            params.add(new NameValuePair("qop", qop));
            params.add(new NameValuePair("nc", nc));
            params.add(new NameValuePair("cnonce", cnonce));
            buffer = new StringBuffer();
            buffer.append("Digest ");
            for (NameValuePair pair : params) {
                if (!pair.getName().equalsIgnoreCase("username")) buffer.append(", ");
                if (pair.getName().equalsIgnoreCase("nc") || pair.getName().equals("qop")) {
                    buffer.append(pair.getPair());
                } else {
                    buffer.append(pair.getQuotedPair());
                }
            }
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
