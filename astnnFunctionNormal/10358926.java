class BackupThread extends Thread {
    public int compareTo(Object o) {
        ChannelPanel chanB = (ChannelPanel) o;
        try {
            int a = Integer.parseInt(this.channel.getName());
            int b = Integer.parseInt(chanB.getChannel().getName());
            return a - b;
        } catch (NumberFormatException nfe) {
            return (this.channel.getName()).compareToIgnoreCase(chanB.getChannel().getName());
        }
    }
}
