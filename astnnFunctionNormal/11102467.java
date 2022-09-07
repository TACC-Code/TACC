class BackupThread extends Thread {
    private void tsaRequest() throws Exception {
        boolean doRun = true;
        do {
            TimeStampRequestGenerator timeStampRequestGenerator = new TimeStampRequestGenerator();
            Random rand = new Random();
            int nonce = rand.nextInt();
            byte[] digest = new byte[20];
            if (instring != null) {
                byte[] digestBytes = instring.getBytes("UTF-8");
                MessageDigest dig = MessageDigest.getInstance(TSPAlgorithms.SHA1, "BC");
                dig.update(digestBytes);
                digest = dig.digest();
                doRun = false;
            }
            if (infilestring != null) {
                digest = digestFile(infilestring, TSPAlgorithms.SHA1);
                doRun = false;
            }
            byte[] hexDigest = Hex.encode(digest);
            System.out.println("MessageDigest=" + new String(hexDigest));
            TimeStampRequest timeStampRequest = timeStampRequestGenerator.generate(TSPAlgorithms.SHA1, digest, BigInteger.valueOf(nonce));
            byte[] requestBytes = timeStampRequest.getEncoded();
            if (outreqstring != null) {
                byte[] outBytes;
                if (base64) {
                    outBytes = Base64.encode(requestBytes);
                } else {
                    outBytes = requestBytes;
                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(outreqstring);
                    fos.write(outBytes);
                } finally {
                    if (fos != null) fos.close();
                }
            }
            URL url;
            URLConnection urlConn;
            DataOutputStream printout;
            DataInputStream input;
            url = new URL(urlstring);
            urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/timestamp-query");
            printout = new DataOutputStream(urlConn.getOutputStream());
            printout.write(requestBytes);
            printout.flush();
            printout.close();
            input = new DataInputStream(urlConn.getInputStream());
            while (input.available() == 0) {
                Thread.sleep(100);
            }
            byte[] ba = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            do {
                if (ba != null) {
                    baos.write(ba);
                }
                ba = new byte[input.available()];
            } while (input.read(ba) != -1);
            byte[] replyBytes = baos.toByteArray();
            if (outrepstring != null) {
                byte[] outBytes;
                if (base64) {
                    outBytes = Base64.encode(replyBytes);
                } else {
                    outBytes = replyBytes;
                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(outrepstring);
                    fos.write(outBytes);
                } finally {
                    if (fos != null) fos.close();
                }
            }
            TimeStampResponse timeStampResponse = new TimeStampResponse(replyBytes);
            timeStampResponse.validate(timeStampRequest);
            System.out.print("TimeStampRequest validated\n");
            Thread.sleep(1000);
        } while (doRun);
    }
}
