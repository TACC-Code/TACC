class BackupThread extends Thread {
    public static String getChannel(final String message) {
        int iterations = -1;
        String tmp = message;
        if (isStatusMessage(message)) iterations = 3; else if (isCommandMessage(message)) iterations = 2;
        for (int i = 0; i < iterations; ++i) tmp = tmp.substring(tmp.indexOf(" ")).trim();
        int index = tmp.indexOf(" ");
        if (index == -1) {
            if (tmp.startsWith(":")) tmp = tmp.substring(1);
            return tmp.trim();
        }
        String ttmp = tmp.substring(0, index).trim();
        if (ttmp.equals("*") || ttmp.equals("@") || ttmp.equals("=")) {
            tmp = tmp.substring(index).trim();
            index = tmp.indexOf(" ");
        } else if (!ttmp.startsWith("#")) return getAuthor(message);
        return tmp.substring(0, index).trim();
    }
}
