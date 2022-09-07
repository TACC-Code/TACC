class BackupThread extends Thread {
    public Request(PipedInputStream pisI, int debugI, String source) {
        pis = pisI;
        debug = debugI;
        source = source;
        boolean done = false;
        Vector headerV = new Vector();
        int nAvail = 0;
        int nRead = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(pis));
        try {
            while (!done) {
                String nextLine = br.readLine();
                if (debug > 4) System.err.println("Request nextLine " + nextLine);
                if (nextLine == null) done = true; else {
                    if (nextLine.length() == 0) done = true;
                    headerV.add(nextLine);
                }
            }
            numHeaderLines = headerV.size();
            headerA = new String[numHeaderLines];
            headerV.copyInto(headerA);
            for (int i = numHeaderLines - 1; i >= 0; i--) {
                if (headerA[i].regionMatches(true, 0, contentLabel, 0, contentLabel.length())) {
                    contentLength = Integer.parseInt(headerA[i].substring(contentLabel.length()));
                } else if (headerA[i].regionMatches(true, 0, modifiedLabel, 0, modifiedLabel.length())) {
                    String modifiedString = headerA[i].substring(modifiedLabel.length());
                    try {
                        modifiedDate = new WebDate(modifiedString);
                    } catch (Exception e) {
                        System.err.println("Exception parsing date: " + modifiedString);
                    }
                }
            }
            if (contentLength > 0) {
                content = new char[contentLength];
                int numRead = br.read(content, 0, contentLength);
                while (numRead < contentLength) {
                    numRead += br.read(content, numRead, contentLength - numRead);
                }
            }
        } catch (Exception e) {
            System.err.println("Request exception: " + e.getMessage());
            isNull = true;
        }
    }
}
