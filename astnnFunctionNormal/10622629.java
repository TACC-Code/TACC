class BackupThread extends Thread {
    private CmsUser updateAdmin(CmsUser bean, CmsUserExt ext, String password, Integer groupId, Integer[] roleIds, Integer[] channelIds) {
        Updater<CmsUser> updater = new Updater<CmsUser>(bean);
        updater.include("email");
        CmsUser user = dao.updateByUpdater(updater);
        user.setGroup(cmsGroupMng.findById(groupId));
        cmsUserExtMng.update(ext, user);
        user.getRoles().clear();
        if (roleIds != null) {
            for (Integer rid : roleIds) {
                user.addToRoles(cmsRoleMng.findById(rid));
            }
        }
        Set<Channel> channels = user.getChannels();
        for (Channel channel : channels) {
            channel.getUsers().remove(user);
        }
        user.getChannels().clear();
        if (channelIds != null) {
            Channel channel;
            for (Integer cid : channelIds) {
                channel = channelMng.findById(cid);
                channel.addToUsers(user);
            }
        }
        unifiedUserMng.update(bean.getId(), password, bean.getEmail());
        return user;
    }
}
