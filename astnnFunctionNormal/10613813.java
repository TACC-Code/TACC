class BackupThread extends Thread {
    public int poll(NetworkInterface iface, Map parameters, org.opennms.netmgt.config.poller.Package pkg) {
        Category log = ThreadCategory.getInstance(getClass());
        int status = SERVICE_UNAVAILABLE;
        InetAddress ipaddr = (InetAddress) iface.getAddress();
        SnmpSession session = null;
        SnmpPeer peer = (SnmpPeer) iface.getAttribute(SNMP_PEER_KEY);
        if (peer == null) throw new RuntimeException("SnmpPeer object not available for interface " + ipaddr);
        int timeout = ParameterMap.getKeyedInteger(parameters, "timeout", peer.getTimeout());
        int retries = ParameterMap.getKeyedInteger(parameters, "retries", peer.getRetries());
        int port = ParameterMap.getKeyedInteger(parameters, "port", DEFAULT_PORT);
        String oid = ParameterMap.getKeyedString(parameters, "oid", DEFAULT_OBJECT_IDENTIFIER);
        String operator = ParameterMap.getKeyedString(parameters, "operator", null);
        String operand = ParameterMap.getKeyedString(parameters, "operand", null);
        peer.setTimeout(timeout);
        peer.setRetries(retries);
        if (log.isDebugEnabled()) log.debug("poll: service= SNMP address= " + ipaddr.getHostAddress() + " port= " + port + " oid=" + oid + " timeout= " + timeout + " retries= " + retries + " operator = " + operator + " operand = " + operand);
        try {
            peer.setPort(port);
            if (log.isDebugEnabled()) {
                String nl = System.getProperty("line.separator");
                log.debug("SnmpMonitor.poll: SnmpPeer configuration: address: " + peer.getPeer() + nl + "      version: " + ((peer.getParameters().getVersion() == SnmpSMI.SNMPV1) ? "SNMPv1" : "SNMPv2") + nl + "      timeout: " + peer.getTimeout() + nl + "      retries: " + peer.getRetries() + nl + "      read commString: " + peer.getParameters().getReadCommunity() + nl + "      write commString: " + peer.getParameters().getWriteCommunity());
            }
            session = new SnmpSession(peer);
        } catch (SocketException e) {
            if (log.isEnabledFor(Priority.ERROR)) log.error("poll: Error creating the SnmpSession to collect from " + ipaddr.getHostAddress(), e);
            if (session != null) {
                try {
                    session.close();
                } catch (Exception ex) {
                    if (log.isInfoEnabled()) log.info("poll: an error occured closing the SNMP session", ex);
                }
            }
            return SERVICE_UNAVAILABLE;
        }
        try {
            SnmpResponseHandler handler = new SnmpResponseHandler();
            SnmpPduPacket out = new SnmpPduRequest(SnmpPduPacket.GETNEXT, new SnmpVarBind[] { new SnmpVarBind(new SnmpObjectId(oid)) });
            synchronized (handler) {
                session.send(out, handler);
                try {
                    handler.wait(timeout * retries + 1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            if (handler.getResult() != null) {
                log.debug("poll: SNMP poll succeeded, addr=" + ipaddr.getHostAddress() + " oid=" + oid + " value=" + handler.getResult().getValue());
                try {
                    status = (meetsCriteria(handler.getResult().getValue(), operator, operand) ? SERVICE_AVAILABLE : SERVICE_UNAVAILABLE);
                } catch (NumberFormatException e) {
                    log.error("Number operator used on a non-number " + e.getMessage());
                    status = SERVICE_AVAILABLE;
                } catch (IllegalArgumentException e) {
                    log.error("Invalid Snmp Criteria: " + e.getMessage());
                    status = SERVICE_UNAVAILABLE;
                }
            } else {
                log.debug("poll: SNMP poll failed, addr=" + ipaddr.getHostAddress() + " oid=" + oid);
                status = SERVICE_UNAVAILABLE;
            }
        } catch (Throwable t) {
            log.warn("poll: Unexpected exception during SNMP poll of interface " + ipaddr.getHostAddress(), t);
            status = SERVICE_UNAVAILABLE;
        } finally {
            try {
                session.close();
            } catch (Exception e) {
                if (log.isEnabledFor(Priority.WARN)) log.warn("collect: An error occured closing the SNMP session for " + ipaddr.getHostAddress(), e);
            }
        }
        return status;
    }
}
