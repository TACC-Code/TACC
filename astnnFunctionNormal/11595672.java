class BackupThread extends Thread {
    void done(Connection c) {
        if (dgcAckNeeded) {
            Connection conn = null;
            Channel ch = null;
            boolean reuse = true;
            DGCImpl.dgcLog.log(Log.VERBOSE, "send ack");
            try {
                ch = c.getChannel();
                conn = ch.newConnection();
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeByte(TransportConstants.DGCAck);
                if (ackID == null) {
                    ackID = new UID();
                }
                ackID.write((DataOutput) out);
                conn.releaseOutputStream();
                conn.getInputStream().available();
                conn.releaseInputStream();
            } catch (RemoteException e) {
                reuse = false;
            } catch (IOException e) {
                reuse = false;
            }
            try {
                if (conn != null) ch.free(conn, reuse);
            } catch (RemoteException e) {
            }
        }
    }
}
