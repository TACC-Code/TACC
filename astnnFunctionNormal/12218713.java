class BackupThread extends Thread {
    private boolean subscribe(Object host, Object jobId, TraceFileId tfId) {
        String hostURL = this.getHostUrl("" + host);
        System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ START");
        int mid;
        int channel;
        MonitorConsumer mc = this.getMonitorConsumer(hostURL);
        System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ mc = " + mc);
        if (mc == null) {
            System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ failed: mc == null!");
            return false;
        }
        try {
            System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ -8 : mc.isAlive: " + mc.isAlive());
            System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ -7");
            TraceFileMetricListener ml = new TraceFileMetricListener(tfId);
            System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ -6");
            mc.addMetricListener(ml);
            System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ -5");
            MonitorArg args[] = new MonitorArg[1];
            System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ -4");
            args[0] = new MonitorArg("jobid", jobId);
            System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ -3");
            MonitorConsumer.CollectResult cr = (MonitorConsumer.CollectResult) mc.collect("application.message", args);
            System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ -2");
            System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ -2.1 : mc.isAlive: " + mc.isAlive());
            cr.waitResult();
            System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ -1");
            if (cr.getStatus() != 0) {
                System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ mc.native -- COLLECT failed: " + cr.getStatusStr());
                return false;
            }
            System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ mc.native -- COLLECT successful: " + cr.getStatusStr());
            System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ 0");
            MonitorConsumer.MetricInstance subscribeMetric = cr.getMetricInstance();
            System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ 1");
            mid = subscribeMetric.getMetricId();
            System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ 2");
            channel = mc.getChannelId();
            System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ 3");
            MonitorConsumer.CommandResult subscribeResult = mc.subscribe(mid, channel);
            System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ 4");
            subscribeResult.waitResult();
            System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ 5");
            if (subscribeResult.getStatus() != 0) {
                System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") [mid: " + mid + "] ~ mc.native -- subscribe failed: " + subscribeResult.getStatusStr());
                return false;
            }
            System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") [mid: " + mid + "] ~ mc.native -- subscribe successful: " + subscribeResult.getStatusStr());
        } catch (MonitorException mex) {
            System.out.println("TF - TraceFileMonitor.subscribe(" + hostURL + "," + jobId + ") ~ mc.native -- subscribe failed, MonitorException: " + mex.getMessage());
            mex.printStackTrace();
            return false;
        } catch (TraceFileException ex) {
            System.out.println("TF - TraceFileMonitor.ssubscribe(" + hostURL + "," + jobId + ") ~ failed - TFException:" + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
        JobToMetricMappingStore mappingStore = JobToMetricMappingStore.getInstance();
        mappingStore.addMapping(host, jobId, mid, channel);
        return true;
    }
}
