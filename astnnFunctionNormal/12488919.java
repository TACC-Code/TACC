class BackupThread extends Thread {
    public void updateBWConfigure(IBWControlContext context) {
        if (!(context instanceof BWContext)) return;
        BWContext c = (BWContext) context;
        IBWControllable bc = c.getBWControllable();
        synchronized (c) {
            if (bc.getBandwidthConfigure() == null) {
                c.bwConfig = null;
                c.lastSchedule = -1;
            } else {
                long[] oldConfig = c.bwConfig;
                c.bwConfig = new long[4];
                for (int i = 0; i < 4; i++) {
                    c.bwConfig[i] = bc.getBandwidthConfigure().getChannelBandwidth()[i];
                }
                if (oldConfig == null) {
                    c.lastSchedule = System.currentTimeMillis();
                    long[] channelInitialBurst = bc.getBandwidthConfigure().getChannelInitialBurst();
                    for (int i = 0; i < 4; i++) {
                        if (channelInitialBurst[i] >= 0) {
                            c.tokenRc[i] = channelInitialBurst[i];
                        } else {
                            c.tokenRc[i] = defaultCapacity / 2;
                        }
                    }
                } else {
                    if (c.bwConfig[IBandwidthConfigure.OVERALL_CHANNEL] >= 0 && oldConfig[IBandwidthConfigure.OVERALL_CHANNEL] < 0) {
                        c.tokenRc[IBandwidthConfigure.OVERALL_CHANNEL] += c.tokenRc[IBandwidthConfigure.AUDIO_CHANNEL] + c.tokenRc[IBandwidthConfigure.VIDEO_CHANNEL] + c.tokenRc[IBandwidthConfigure.DATA_CHANNEL];
                        for (int i = 0; i < 3; i++) {
                            c.tokenRc[i] = 0;
                        }
                    } else if (c.bwConfig[IBandwidthConfigure.OVERALL_CHANNEL] < 0 && oldConfig[IBandwidthConfigure.OVERALL_CHANNEL] >= 0) {
                        for (int i = 0; i < 3; i++) {
                            if (c.bwConfig[i] >= 0) {
                                c.tokenRc[i] += c.tokenRc[IBandwidthConfigure.OVERALL_CHANNEL];
                                break;
                            }
                        }
                        c.tokenRc[IBandwidthConfigure.OVERALL_CHANNEL] = 0;
                    }
                }
            }
        }
    }
}
