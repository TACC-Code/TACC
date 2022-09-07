class BackupThread extends Thread {
    @Override
    public void initialize(Properties arg0) {
        this.channels = new HashSet<ManagedReference<Channel>>();
        this.rooms = new HashSet<ManagedReference<Room>>();
        logger.info("Inicializando Canais e Rooms");
        DAOMap dao = new DAOMap();
        try {
            dao.selectAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator<Map> it = dao.getVos().iterator();
        ChannelManager channelMgr = AppContext.getChannelManager();
        while (it.hasNext()) {
            Map map = it.next();
            Channel c1 = channelMgr.createChannel(("map_" + map.getId()), new ManagerChannelPlayer(), Delivery.RELIABLE);
            ManagedReference<Channel> channel1 = AppContext.getDataManager().createReference(c1);
            this.channels.add(channel1);
            Room room = new Room("Room" + map.getId(), "Room" + map.getId());
            DataManager dataManager = AppContext.getDataManager();
            ManagedReference<Room> r = dataManager.createReference(room);
            rooms.add(r);
            System.out.println("map_" + map.getId());
            System.out.println("Room" + map.getId());
        }
        logger.info("-- JMMORPG Initialized ---");
    }
}
