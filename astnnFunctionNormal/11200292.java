class BackupThread extends Thread {
    public void itemAdded(ItemIF parm1) {
        String itemTitle = parm1.getTitle();
        ChannelIF chan = parm1.getChannel();
        theMan.newItemInChannel(chan, itemTitle);
        log.info("ItemAdded");
    }
}
