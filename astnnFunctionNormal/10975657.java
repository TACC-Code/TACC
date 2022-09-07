class BackupThread extends Thread {
    private void openConnection() throws IOException {
        connection = (HttpURLConnection) url.openConnection(proxy);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml; charset=" + XmlRpcMessages.getString("XmlRpcClient.Encoding"));
        if (hasBasicAuth()) {
            String data = this.username + ":" + this.password;
            String encoded = new String();
            for (char c : Base64.encode(data.getBytes())) encoded += c;
            connection.setRequestProperty("Authorization", "Basic " + encoded);
        }
        if (requestProperties != null) {
            for (Iterator propertyNames = requestProperties.keySet().iterator(); propertyNames.hasNext(); ) {
                String propertyName = (String) propertyNames.next();
                connection.setRequestProperty(propertyName, (String) requestProperties.get(propertyName));
            }
        }
    }
}
