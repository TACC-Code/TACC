class BackupThread extends Thread {
    public List getCategorizedChannels(ICategory category) {
        List channelObjecten = null;
        if (category == null) {
            channelObjecten = getChannels(IChannelDAO.CATEGORIZED_CHANNELS);
        } else {
            if (queries.getProperty(IChannelDAO.SPECIFIC_CATEGORIZED_CHANNELS) != null) {
                StringBuffer sb = new StringBuffer();
                sb.append(queries.getProperty(IChannelDAO.SPECIFIC_CATEGORIZED_CHANNELS));
                sb.append("(").append(category.getCategoryAndChildrenIds()).append(")");
                channelObjecten = this.getHibernateTemplate().find(sb.toString());
            }
        }
        List channels = new ArrayList(channelObjecten.size());
        for (int i = 0; i < channelObjecten.size(); i++) {
            channels.add(((Object[]) channelObjecten.get(i))[0]);
        }
        return channels;
    }
}
