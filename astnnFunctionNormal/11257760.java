class BackupThread extends Thread {
        private void sendLines(int count) {
            Channel channel = getChannel();
            if (count == 1) {
                channel.send(new OneLineAddedMessage());
            }
            if (count >= 4) {
                channel.send(new FourLinesAddedMessage());
                sendLines(count - 4);
            } else if (count >= 2) {
                channel.send(new TwoLinesAddedMessage());
                sendLines(count - 2);
            }
        }
}
