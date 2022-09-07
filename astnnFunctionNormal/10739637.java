class BackupThread extends Thread {
    @Override
    public void execute(PaymentInfoMagcard payinfo) {
        String sReturned = "";
        URL url;
        System.setProperty("javax.net.ssl.keyStore", sClientCertPath);
        System.setProperty("javax.net.ssl.keyStorePassword", sPasswordCert);
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
        try {
            url = new URL("https://" + HOST + ":" + PORT);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setHostnameVerifier(new NullHostNameVerifier());
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            StringBuilder xml = createOrder(payinfo);
            String a = xml.toString();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(xml.toString().getBytes());
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            sReturned = in.readLine();
        } catch (IOException exIoe) {
            payinfo.paymentError(LocalRes.getIntString("exception.iofile"), exIoe.getMessage());
        }
        LinkPointParser lpp = new LinkPointParser(sReturned);
        Map props = lpp.splitXML();
        if (lpp.getResult().equals(LocalRes.getIntString("button.ok"))) {
            if (APPROVED.equals(props.get("r_approved"))) {
                payinfo.paymentOK((String) props.get("r_code"), (String) props.get("r_ordernum"), sReturned);
            } else {
                payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), (String) props.get("r_error"));
            }
        } else {
            payinfo.paymentError(lpp.getResult(), "");
        }
    }
}
