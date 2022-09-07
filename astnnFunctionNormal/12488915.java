class BackupThread extends Thread {
    public IBWControlContext registerBWControllable(IBWControllable bc) {
        BWContext context = new BWContext(bc);
        long[] channelInitialBurst = null;
        if (bc.getBandwidthConfigure() != null) {
            context.bwConfig = new long[4];
            for (int i = 0; i < 4; i++) {
                context.bwConfig[i] = bc.getBandwidthConfigure().getChannelBandwidth()[i];
            }
            channelInitialBurst = bc.getBandwidthConfigure().getChannelInitialBurst();
        }
        context.buckets[0] = new Bucket(bc, 0);
        context.buckets[1] = new Bucket(bc, 1);
        context.buckets[2] = new Bucket(bc, 2);
        context.tokenRc = new double[4];
        if (context.bwConfig != null) {
            for (int i = 0; i < 4; i++) {
                if (channelInitialBurst[i] >= 0) {
                    context.tokenRc[i] = channelInitialBurst[i];
                } else {
                    context.tokenRc[i] = defaultCapacity / 2;
                }
            }
            context.lastSchedule = System.currentTimeMillis();
        } else {
            context.lastSchedule = -1;
        }
        contextMap.put(bc, context);
        return context;
    }
}
