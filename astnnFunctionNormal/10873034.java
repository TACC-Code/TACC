class BackupThread extends Thread {
    public String vote(String serverName, String owner, String prefix, int option, String spreadusername) {
        try {
            HttpConnection c = null;
            DataInputStream is = null;
            DataOutputStream dstream = null;
            StringBuffer b = new StringBuffer();
            String sysProp = getSystemPropertyString();
            try {
                c = (HttpConnection) Connector.open(voteURL(serverName));
                c.setRequestMethod(HttpConnection.POST);
                c.setRequestProperty("Connection", "Keep-Alive");
                c.setRequestProperty("User-Agent", "Profile/MIDP-2.0 Confirguration/CLDC-1.1");
                c.setRequestProperty("Content-Type", "multipart/form-data; boundary=****4353");
                dstream = new DataOutputStream(c.openOutputStream());
                dstream.write("--****4353\r\n".getBytes());
                dstream.write(("Content-Disposition: form-data; name=option" + "\r\n\r\n" + option + "\r\n" + "--****4353\r\n").getBytes());
                dstream.write(("Content-Disposition: form-data; name=systemProperty" + "\r\n\r\n" + sysProp.trim() + "\r\n" + "--****4353\r\n").getBytes());
                dstream.write(("Content-Disposition: form-data; name=owner" + "\r\n\r\n" + owner.trim() + "\r\n" + "--****4353\r\n").getBytes());
                dstream.write(("Content-Disposition: form-data; name=spreadusername" + "\r\n\r\n" + spreadusername.trim() + "\r\n" + "--****4353\r\n").getBytes());
                dstream.write(("Content-Disposition: form-data; name=prefix" + "\r\n\r\n" + prefix.trim() + "\r\n" + "--****4353--\r\n\r\n").getBytes());
                is = c.openDataInputStream();
                int ch;
                while ((ch = is.read()) != -1) {
                    b.append((char) ch);
                }
                return b.toString().trim();
            } catch (Exception e) {
                throw new IllegalArgumentException("Not an HTTP URL");
            } finally {
                if (is != null) is.close();
                if (dstream != null) dstream.close();
                if (c != null) c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "dead";
    }
}
