class BackupThread extends Thread {
    @Override
    public void execute() throws BuildException {
        if (name == null || name.length() == 0) throw new BuildException("Name attribute cannot be empty!");
        if (userId == null || userId.length() == 0) throw new BuildException("User attribute cannot be empty!");
        if (email == null || email.length() == 0) throw new BuildException("Email attribute cannot be empty!");
        if (descriptor == null || !descriptor.isFile()) throw new BuildException("Invalid SageTV plugin descriptor!");
        if (reqType == null) throw new BuildException("Request type cannot be null!");
        if (pluginId == null || pluginId.length() == 0) throw new BuildException("Plugin ID attribute cannot be empty!");
        if (descriptor == null || !descriptor.isFile()) throw new BuildException("Plugin descriptor does not exist!");
        if (xmlEnc == null || xmlEnc.length() == 0) xmlEnc = "UTF-8";
        try {
            StringBuilder payload = new StringBuilder();
            payload.append("Name=" + URLEncoder.encode(name, "UTF-8"));
            payload.append("&Email=" + URLEncoder.encode(email, "UTF-8"));
            payload.append("&Username=" + URLEncoder.encode(userId, "UTF-8"));
            payload.append("&PluginID=" + URLEncoder.encode(pluginId, "UTF-8"));
            payload.append("&RequestType=" + URLEncoder.encode(reqType.getVal(), "UTF-8"));
            BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(descriptor), xmlEnc));
            String line;
            StringBuilder doc = new StringBuilder();
            while ((line = r.readLine()) != null) doc.append(line + "\n");
            r.close();
            payload.append("&Manifest=" + URLEncoder.encode(doc.toString(), "UTF-8"));
            URL url = new URL("http://download.sage.tv/pluginSubmit.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(payload.toString());
            out.flush();
            out.close();
            r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while (r.readLine() != null) ;
            r.close();
            if (conn.getResponseCode() != 200) throw new BuildException("Invalid response received from SageTV [" + conn.getResponseMessage() + "]");
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }
}
