class BackupThread extends Thread {
    public void handle(JabberConnection jc) throws IOException {
        if (type.equals("groupchat")) {
            System.err.println("##### gc mess recieved.");
            String f = jc.exCN(getFrom());
            if (getBody() != null && getBody().length() != 0) {
                System.err.println("##### handing message to groupchat");
                jc.getChat(f).processIncoming(jc.exN(getFrom()), getBody());
            } else {
                System.err.println("#### message is empty");
            }
            if (getSubject() != null && getSubject().length() != 0) {
                jc.getChat(f).processIncoming(jc.exN(getFrom()), "", getSubject());
            }
        } else if (type.equals("chat")) {
            System.err.println("##### private chat mess recieved.");
            String f = jc.exCN(getFrom());
            String thread = getThread();
            String body = getBody();
            if (body != null && body.length() != 0) {
                System.err.println("##### handing message to private chat");
                String t = "";
                if (thread != null) {
                    t = thread;
                }
                String from1 = getFrom();
                if (jc.isChat(from1) | jc.isPrivateChat(from1)) {
                    from1 = from1.substring(0, from1.indexOf("@"));
                } else {
                    from1 = from1.substring(from1.indexOf("/") + 1);
                }
                jc.fireEvent(this, "privateChat", new Object[] { getFrom(), t, from1, body });
            } else {
                System.err.println("#### message is empty");
            }
            jc.messageList.add(this);
        } else if (type.equals("normal") || type.equals("")) {
            System.err.println("##### private mess recieved.");
            String f = jc.exCN(getFrom());
            String body = getBody();
            String subject = getSubject();
            if (body != null && body.length() != 0) {
                System.err.println("##### displaying mesasage");
                jc.fireEvent(this, "displayMessage", this);
            } else {
                System.err.println("#### message is empty");
            }
            jc.messageList.add(this);
        } else if (type.equals("headline")) {
            jc.messageList.add(this);
            jc.fireEvent(this, "displayHeadline", this);
        } else if (type.equals("error")) {
            Chat gpc = jc.getChannelFor(getFrom());
            String error = getError();
            String t = "";
            if (getThread() != null) t = getThread();
            jc.getPrivateChat(getFrom(), true, t);
        }
        jc.fireEvent(this, "handleIQ", this);
        System.gc();
    }
}
