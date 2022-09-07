class BackupThread extends Thread {
    public void sendFile(String f) {
        try {
            URL u = new URL("http://129.206.229.148/deegreeWPS/all");
            HttpURLConnection urlc = (HttpURLConnection) u.openConnection();
            urlc.setReadTimeout(Navigator.TIME_OUT);
            urlc.setAllowUserInteraction(false);
            urlc.setRequestMethod("POST");
            urlc.setRequestProperty("Content-Type", "application/xml");
            urlc.setDoOutput(true);
            urlc.setDoInput(true);
            urlc.setUseCaches(false);
            PrintWriter xmlOut = null;
            xmlOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(urlc.getOutputStream())));
            xmlOut = new java.io.PrintWriter(urlc.getOutputStream());
            xmlOut.write(f);
            xmlOut.flush();
            xmlOut.close();
            InputStream is = urlc.getInputStream();
            response = "";
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = rd.readLine()) != null) {
                response = response + line;
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
