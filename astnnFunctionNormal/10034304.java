class BackupThread extends Thread {
    private void configureMulticastParameters() throws ClusteringFault {
        Properties mcastProps = channel.getMembershipService().getProperties();
        Parameter mcastAddress = getParameter(TribesConstants.MCAST_ADDRESS);
        if (mcastAddress != null) {
            mcastProps.setProperty(TribesConstants.MCAST_ADDRESS, ((String) mcastAddress.getValue()).trim());
        }
        Parameter mcastBindAddress = getParameter(TribesConstants.MCAST_BIND_ADDRESS);
        if (mcastBindAddress != null) {
            mcastProps.setProperty(TribesConstants.MCAST_BIND_ADDRESS, ((String) mcastBindAddress.getValue()).trim());
        }
        Parameter mcastPort = getParameter(TribesConstants.MCAST_PORT);
        if (mcastPort != null) {
            mcastProps.setProperty(TribesConstants.MCAST_PORT, ((String) mcastPort.getValue()).trim());
        }
        Parameter mcastFrequency = getParameter(TribesConstants.MCAST_FREQUENCY);
        if (mcastFrequency != null) {
            mcastProps.setProperty(TribesConstants.MCAST_FREQUENCY, ((String) mcastFrequency.getValue()).trim());
        }
        Parameter mcastMemberDropTime = getParameter(TribesConstants.MEMBER_DROP_TIME);
        if (mcastMemberDropTime != null) {
            mcastProps.setProperty(TribesConstants.MEMBER_DROP_TIME, ((String) mcastMemberDropTime.getValue()).trim());
        }
        ReceiverBase receiver = (ReceiverBase) channel.getChannelReceiver();
        Parameter tcpListenHost = getParameter(TribesConstants.LOCAL_MEMBER_HOST);
        if (tcpListenHost != null) {
            String host = ((String) tcpListenHost.getValue()).trim();
            mcastProps.setProperty(TribesConstants.TCP_LISTEN_HOST, host);
            mcastProps.setProperty(TribesConstants.BIND_ADDRESS, host);
            receiver.setAddress(host);
        } else {
            String host;
            try {
                host = Utils.getIpAddress();
            } catch (SocketException e) {
                String msg = "Could not get local IP address";
                log.error(msg, e);
                throw new ClusteringFault(msg, e);
            }
            mcastProps.setProperty(TribesConstants.TCP_LISTEN_HOST, host);
            mcastProps.setProperty(TribesConstants.BIND_ADDRESS, host);
            receiver.setAddress(host);
        }
        String localIP = System.getProperty(ClusteringConstants.LOCAL_IP_ADDRESS);
        if (localIP != null) {
            receiver.setAddress(localIP);
        }
        Parameter tcpListenPort = getParameter(TribesConstants.LOCAL_MEMBER_PORT);
        if (tcpListenPort != null) {
            String port = ((String) tcpListenPort.getValue()).trim();
            mcastProps.setProperty(TribesConstants.TCP_LISTEN_PORT, port);
            receiver.setPort(Integer.parseInt(port));
        }
        mcastProps.setProperty(TribesConstants.MCAST_CLUSTER_DOMAIN, new String(domain));
    }
}
