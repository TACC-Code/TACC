class BackupThread extends Thread {
    public void postTime(double time) {
        super.postTime(time);
        WaypointGroup points = new WaypointGroup();
        for (String chName : subscribedChannels()) {
            System.out.println(chName + " " + rbnbController.getChannel(chName));
        }
        Iterator i = channels.iterator();
        while (i.hasNext()) {
            String channelName = (String) i.next();
            int channelIndex = channelMap.GetIndex(channelName);
            if (channelIndex != -1) {
                double lat = channelMap.GetDataAsFloat64(channelIndex)[0];
                double lng = channelMap.GetDataAsFloat64(channelIndex)[1];
                points.add(lat, lng);
            }
        }
        if (!points.isEmpty()) map.draw(points);
    }
}
