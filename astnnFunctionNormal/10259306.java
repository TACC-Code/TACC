class BackupThread extends Thread {
        private void sendResponse(String status, String mime, java.util.Properties header, InputStream data) {
            try {
                if (status == null) throw new Error("sendResponse(): Status can't be null.");
                OutputStream out = mySocket.getOutputStream();
                PrintWriter pw = new PrintWriter(out);
                pw.print("HTTP/1.0 " + status + " \r\n");
                if (mime != null) pw.print("Content-Type: " + mime + "\r\n");
                if (header == null || header.getProperty("Date") == null) pw.print("Date: " + gmtFrmt.format(new Date()) + "\r\n");
                if (header != null) {
                    header.remove("Content-Length");
                    Enumeration<Object> e = header.keys();
                    while (e.hasMoreElements()) {
                        String key = (String) e.nextElement();
                        String value = header.getProperty(key);
                        pw.print(key + ": " + value + "\r\n");
                    }
                }
                if (data != null) {
                    ByteArrayOutputStream contentBytes = new ByteArrayOutputStream();
                    OutputStream contentOut = contentBytes;
                    if ("gzip".equals(header.getProperty("Content-Encoding"))) {
                        contentOut = new GZIPOutputStream(contentBytes);
                    }
                    byte[] buff = new byte[2048];
                    while (true) {
                        int read = data.read(buff, 0, buff.length);
                        if (read <= 0) break;
                        contentOut.write(buff, 0, read);
                    }
                    contentOut.flush();
                    contentOut.close();
                    pw.print("Content-Length: " + contentBytes.size() + "\r\n");
                    pw.print("\r\n");
                    pw.flush();
                    contentBytes.writeTo(out);
                    data.close();
                } else {
                    pw.print("\r\n");
                }
                out.flush();
                out.close();
            } catch (IOException ioe) {
                log.log(Level.SEVERE, "Error sending response", ioe);
                try {
                    mySocket.close();
                } catch (Throwable t) {
                }
            }
        }
}
