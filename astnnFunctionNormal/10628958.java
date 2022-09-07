class BackupThread extends Thread {
    public HttpResponse execute(long startPosition, long endPosition, int timeout) throws IOException {
        ArrayList socketList = (ArrayList) cachedSockets.get(host + hostPort);
        Socket server = null;
        if (socketList != null && socketList.size() > 0) server = (Socket) socketList.remove(0); else {
            server = new Socket(host, hostPort);
            server.setSoTimeout(timeout);
        }
        BufferedOutputStream serverOut = new BufferedOutputStream(server.getOutputStream());
        PrintWriter serverPrintOut = new PrintWriter(serverOut);
        serverPrintOut.write(firstReqLine + "\r\n");
        HashMap<String, String> newHeaders = (HashMap<String, String>) headers.clone();
        if (startPosition > 0 || endPosition > 0) {
            newHeaders.put("range", "bytes=" + (originalStart + startPosition) + "-" + (originalStart + endPosition));
        }
        Iterator it = newHeaders.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            serverPrintOut.write(key + ": " + value + "\r\n");
        }
        serverPrintOut.write("\r\n");
        serverPrintOut.flush();
        if (requestBody != null) {
            serverOut.write(requestBody);
            serverOut.flush();
        }
        HttpResponse response = new HttpResponse(server);
        return response;
    }
}
