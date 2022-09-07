class BackupThread extends Thread {
        private static Enumeration parseConfigFile(URL url) {
            try {
                InputStream in = url.openStream();
                Reader r;
                try {
                    r = new InputStreamReader(in, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    r = new InputStreamReader(in, "UTF8");
                }
                r = new BufferedReader(r);
                Vector tokens = new Vector();
                StringBuffer tokenBuf = new StringBuffer();
                int state = START;
                for (; ; ) {
                    int n = r.read();
                    if (n < 0) break;
                    char c = (char) n;
                    switch(c) {
                        case '\r':
                        case '\n':
                            state = START;
                            break;
                        case ' ':
                        case '\t':
                            break;
                        case '#':
                            state = IN_COMMENT;
                            break;
                        default:
                            if (state != IN_COMMENT) {
                                state = IN_NAME;
                                tokenBuf.append(c);
                            }
                            break;
                    }
                    if (tokenBuf.length() != 0 && state != IN_NAME) {
                        tokens.addElement(tokenBuf.toString());
                        tokenBuf.setLength(0);
                    }
                }
                if (tokenBuf.length() != 0) tokens.addElement(tokenBuf.toString());
                return tokens.elements();
            } catch (IOException e) {
                return null;
            }
        }
}
