class BackupThread extends Thread {
    public void handle(Kernel kernel, DatagramComponent comp, SocketAddress sa, byte[] data) {
        try {
            Integer shortID = comp.getShortID(sa);
            if (shortID == null) {
                return;
            }
            NioComponent nioComp = kernel.getComponent(NioComponent.class);
            INonBlockingConnection nioConn = nioComp.getConnection(shortID);
            Store store = (Store) nioConn.getAttachment();
            ClassroomsComponent csComp = kernel.getComponent(ClassroomsComponent.class);
            EChnlMsg m = new EChnlMsg();
            m.deserialize(data, 22);
            RuntimeClassroom room = csComp.getClassroom(m.getChannelName());
            StatusCode result = StatusCode.Unknown;
            RuntimeMember member = null;
            if (room != null) {
                member = room.getMember(store.NAME);
                if (member != null) {
                    member.enter(nioConn, sa);
                    result = StatusCode.OK;
                } else {
                    result = StatusCode.EntryDenied;
                }
                store.MEMBER = member;
            } else {
                result = StatusCode.NotFound;
            }
            EChnlRsp f = new EChnlRsp();
            f.setStatusCode(result);
            if (room != null) {
                ChannelBean bean = new ChannelBean();
                bean.setHashCode(room.hashCode());
                bean.setName(room.getName());
                if (member != null) {
                    bean.setRole(member.getRole());
                }
                f.setChannelBean(bean);
            }
            comp.sendReliable(nioConn, sa, f);
            if (member != null && member.isEntered() && (member.getConnection() != null)) {
                NPeerMsg r = new NPeerMsg();
                PeerBean bean = new PeerBean();
                bean.setHashCode(member.getConnection().hashCode());
                bean.setName(member.getName());
                bean.setRole(member.getRole());
                r.setBean(bean);
                comp.broadcastReliable(member, r);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
