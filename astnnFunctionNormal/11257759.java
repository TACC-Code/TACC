class BackupThread extends Thread {
        public void run() {
            Settings settings = getChannel().getConfig().getSettings();
            if (stopWatch.getTime() >= nextTriggerTime) {
                if (!suddenDeathEnabled) {
                    suddenDeathEnabled = true;
                    GmsgMessage gmsg = new GmsgMessage();
                    String message = settings.getSuddenDeathMessage();
                    if (message.startsWith("key:")) {
                        gmsg.setKey(message.substring(4));
                    } else {
                        gmsg.setText(message);
                    }
                    getChannel().send(gmsg);
                    GmsgMessage rate = new GmsgMessage();
                    rate.setKey("filter.suddendeath.rate", settings.getSuddenDeathLinesAdded(), settings.getSuddenDeathDelay());
                    getChannel().send(rate);
                }
                sendLines(settings.getSuddenDeathLinesAdded());
                nextTriggerTime = nextTriggerTime + settings.getSuddenDeathDelay() * 1000;
            } else if (stopWatch.getTime() >= nextWarningTime && !suddenDeathEnabled) {
                GmsgMessage gmsg = new GmsgMessage();
                gmsg.setKey("filter.suddendeath.warning", Math.ceil((nextTriggerTime - stopWatch.getTime()) / 1000d));
                getChannel().send(gmsg);
                nextWarningTime = nextWarningTime + WARNING_DELAY * 1000;
            }
        }
}
