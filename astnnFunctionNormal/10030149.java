class BackupThread extends Thread {
    public void submitReport() {
        String subject = m_Subject.getText();
        String description = m_Description.getText();
        String email = m_Email.getText();
        if (subject.length() == 0) {
            Util.flashComponent(m_Subject, Color.RED);
            return;
        }
        if (description.length() == 0) {
            Util.flashComponent(m_Description, Color.RED);
            return;
        }
        DynamicLocalisation loc = m_MainFrame.getLocalisation();
        if (email.length() == 0 || email.indexOf("@") == -1 || email.indexOf(".") == -1 || email.startsWith("@")) {
            email = "anonymous@blaat.com";
        }
        try {
            subject = URLEncoder.encode("Bug report: " + subject, "UTF-8");
            description = URLEncoder.encode("A bug report has been submitted: \n\n" + description, "UTF-8");
            URL url = new URL("http://www.leviaswrath.org/mail.php" + "?from=" + email + "&to=" + Constants.BUG_EMAIL + "&subject=" + subject + "&body=" + description);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseMessage().equals("OK")) {
                JOptionPane.showMessageDialog(this, loc.getMessage("ReportBug.SentMessage"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dispose();
    }
}
