class BackupThread extends Thread {
    public void configureRoutes(GMChannelRoute route, boolean percussionChannel) {
        List conflictingRoutes = null;
        if (this.midiChannels.contains(route)) {
            this.midiChannels.remove(route);
        }
        if (percussionChannel) {
            route.setChannel1(PERCUSSION_CHANNEL);
            route.setChannel2(PERCUSSION_CHANNEL);
        } else {
            if (route.getChannel1() >= 0) {
                if (route.getChannel2() < 0) {
                    route.setChannel2(route.getChannel1());
                }
                conflictingRoutes = findConflictingRoutes(route);
            } else {
                List freeChannels = getFreeChannels();
                route.setChannel1((freeChannels.size() > 0 ? ((Integer) freeChannels.get(0)).intValue() : GMChannelRoute.NULL_VALUE));
                route.setChannel2((freeChannels.size() > 1 ? ((Integer) freeChannels.get(1)).intValue() : route.getChannel1()));
            }
        }
        this.midiChannels.add(route);
        if (conflictingRoutes != null) {
            Iterator it = conflictingRoutes.iterator();
            while (it.hasNext()) {
                GMChannelRoute conflictingRoute = (GMChannelRoute) it.next();
                conflictingRoute.setChannel1(GMChannelRoute.NULL_VALUE);
                conflictingRoute.setChannel2(GMChannelRoute.NULL_VALUE);
                configureRoutes(conflictingRoute, false);
            }
        }
    }
}
