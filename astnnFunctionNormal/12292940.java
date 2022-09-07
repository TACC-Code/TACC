class BackupThread extends Thread {
    public static void send(String from, String to, String message, String subject) {
        try {
            ResourceBundle appProps = ResourceBundle.getBundle("epice");
            URL url = new URL(appProps.getString("ASYNC_SERVICES") + "Controller?action=sendEmail&from=" + URLEncoder.encode(from, "UTF8") + "&to=" + URLEncoder.encode(to, "UTF8") + "&message=" + URLEncoder.encode(message, "UTF8") + "&subject=" + URLEncoder.encode(subject, "UTF8") + "&smtpUser=" + appProps.getString("SMTP_USER") + "&smtpPassword=" + appProps.getString("SMTP_PASSWORD") + "&smtpHost=" + appProps.getString("SMTP_HOST") + "&smtpPort=" + appProps.getString("SMTP_PORT") + "&smtpStartTlsEnable=" + appProps.getString("SMTP_STARTTLS_ENABLE") + "&smtpAuth=" + appProps.getString("SMTP_AUTH") + "&smtpSocketFactoryPort=" + appProps.getString("SMTP_SOCKETFACTORY_PORT") + "&smtpSocketFactoryClass=" + appProps.getString("SMTP_SOCKETFACTORY_CLASS") + "&smtpSocketFactoryFallback=" + appProps.getString("SMTP_SOCKETFACTORY_FALLBACK"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            String msg = conn.getResponseMessage();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SendMailUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(SendMailUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SendMailUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
