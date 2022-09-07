class BackupThread extends Thread {
    public static ArrayList<String> getChannels(IRCMessage msg) {
        StringTokenizer t = new StringTokenizer(msg.getArgs().get(0), ",", false);
        ArrayList<String> list = new ArrayList<String>(20);
        while (t.hasMoreTokens()) list.add(t.nextToken());
        return list;
    }
}
