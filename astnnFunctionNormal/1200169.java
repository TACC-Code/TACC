class BackupThread extends Thread {
    public List findConflictingRoutes(GMChannelRoute gmChannelRoute) {
        List routes = new ArrayList();
        Iterator it = this.midiChannels.iterator();
        while (it.hasNext()) {
            GMChannelRoute route = (GMChannelRoute) it.next();
            if (!route.equals(gmChannelRoute)) {
                if (route.getChannel1() == gmChannelRoute.getChannel1() || route.getChannel1() == gmChannelRoute.getChannel2() || route.getChannel2() == gmChannelRoute.getChannel1() || route.getChannel2() == gmChannelRoute.getChannel2()) {
                    routes.add(route);
                }
            }
        }
        return routes;
    }
}
