class BackupThread extends Thread {
    public void dispatchAgent(Resource struct, Ticket ticket) throws TicketNotSupportedException {
        BufferedOutputStream out;
        HttpURLConnection conn;
        java.net.URL url;
        URL[] targets;
        int n;
        if (ticket == null || struct == null) {
            throw new NullPointerException("Ticket or Resource!");
        }
        targets = ticket.getTarget(protocol());
        if (targets.length == 0) {
            throw new TicketNotSupportedException("Bad ticket, no \"" + protocol() + "\" target specified!");
        }
        try {
            url = new java.net.URL(targets[0].toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("content-type", MIME_TYPE);
            conn.setRequestProperty("User-Agent", "SeMoA-HttpOutGate");
            out = new BufferedOutputStream(conn.getOutputStream(), BUF_SIZE);
            Resources.zip(struct, out);
            out.flush();
            n = conn.getResponseCode();
            if (n == 200) {
                return;
            }
            throw new TicketNotSupportedException("[" + protocol() + "] Server returned error " + n);
        } catch (IOException e) {
            throw new TicketNotSupportedException(e.getMessage());
        }
    }
}
