class BackupThread extends Thread {
        public void run() {
            ChannelList chanList = new ChannelList(null);
            int chanCount = chanList.getChannelCount();
            for (int i = 0; i < chanCount; i++) {
                Channel chan = chanList.getChannelAt(i);
                System.out.print("Channel #" + (i + 1) + " - ");
                System.out.println(chan.getChannelName());
            }
            busyIconTimer.stop();
            statusAnimationLabel.setIcon(idleIcon);
            statusMessageLabel.setText("");
        }
}
