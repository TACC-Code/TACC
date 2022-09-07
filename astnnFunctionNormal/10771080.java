class BackupThread extends Thread {
    public ChannelTxt update(ChannelTxt txt, Channel channel) {
        ChannelTxt entity = dao.findById(channel.getId());
        if (entity == null) {
            entity = save(txt, channel);
            channel.getChannelTxtSet().add(entity);
            return entity;
        } else {
            if (txt.isAllBlank()) {
                channel.getChannelTxtSet().clear();
                return null;
            } else {
                Updater<ChannelTxt> updater = new Updater<ChannelTxt>(txt);
                entity = dao.updateByUpdater(updater);
                entity.blankToNull();
                return entity;
            }
        }
    }
}
