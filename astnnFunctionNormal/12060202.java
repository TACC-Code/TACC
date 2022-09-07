class BackupThread extends Thread {
    public List<ChannelDto> getChannelTree(ChannelDto parent) {
        List result = new ArrayList();
        if (parent == null) {
        }
        ChannelDto d1 = new ChannelDto();
        d1.setName("首页");
        d1.setId(1l);
        ChannelDto d2 = new ChannelDto();
        d2.setName("新闻");
        d2.setId(2l);
        d2.setChildren(getChildren());
        result.add(d1);
        result.add(d2);
        return result;
    }
}
