class BackupThread extends Thread {
    @Override
    public List<Schedule> getDaySchedule(Date day, Set<String> channels) {
        List<Schedule> retValue = new ArrayList<Schedule>();
        for (String channelCode : channels) {
            Channel channel = channelService.getChannelByCode(channelCode);
            if (channel != null) {
                retValue.add(service.getSchedule(channel, day));
            }
        }
        return retValue;
    }
}
