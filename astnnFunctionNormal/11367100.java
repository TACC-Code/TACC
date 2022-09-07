class BackupThread extends Thread {
    public void printStatus() {
        String[] chs;
        try {
            if (wikimediaBot != null) {
                System.out.println("wikimediaBot connected    : " + wikimediaBot.isConnected());
                System.out.println("wikimediaBot connected to : " + wikimediaBot.getServer());
                chs = wikimediaBot.getChannels();
                for (int j = 0; j < chs.length; j++) {
                    System.out.println("wikimediaBot at           : " + chs[j]);
                }
            }
            if (freenodeBot != null) {
                System.out.println("\n");
                System.out.println("freenodeBot connected    : " + freenodeBot.isConnected());
                System.out.println("freenodeBot connected to : " + freenodeBot.getServer());
                chs = freenodeBot.getChannels();
                for (int j = 0; j < chs.length; j++) {
                    System.out.println("freenodeBot at           : " + chs[j]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
