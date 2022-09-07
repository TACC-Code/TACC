class BackupThread extends Thread {
    public Object execute(String method, Object... parameters) throws Exception {
        OutputStream os = null;
        InputStream is = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(3000);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();
        os = connection.getOutputStream();
        oos = new ObjectOutputStream(os);
        oos.writeObject(method);
        oos.writeObject(parameters);
        oos.flush();
        oos.close();
        os.flush();
        os.close();
        is = connection.getInputStream();
        ois = new ObjectInputStream(is);
        Object r = ois.readObject();
        ois.close();
        is.close();
        connection.disconnect();
        return r;
    }
}
