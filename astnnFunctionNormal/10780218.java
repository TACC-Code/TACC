class BackupThread extends Thread {
    @Override
    public Schedule retrieve() {
        Map<String, Object> attributes = getRequestAttributes();
        String channel = (String) attributes.get("channel");
        String dateString = (String) attributes.get("date");
        DateFormat format = new SimpleDateFormat("yyyyMMddZ");
        Date date;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return service.getSchedule(channelService.getChannelByCode(channel), date);
    }
}
