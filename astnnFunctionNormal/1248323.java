class BackupThread extends Thread {
    @Override
    public ClientSessionListener loggedIn(ClientSession session) {
        if (session == null) {
            throw new NullPointerException("null session");
        }
        logger.log(Level.INFO, "JMMORPG Client login: {0}", session.getName());
        DAOPlayer dao = new DAOPlayer();
        DAOClasse daoClasse = new DAOClasse();
        DAORace daoRace = new DAORace();
        DAOMap daomap = new DAOMap();
        Player player = null;
        Map map = null;
        ManagerSessionPlayer SessionPlayer = null;
        try {
            dao.selectByLogin(session.getName());
            if (dao.getVos() != null && !dao.getVos().isEmpty()) {
                player = dao.getVos().firstElement();
                System.out.println(">>>>" + player.getName());
                daoClasse.selectByPK(player.getClasseId());
                Classe classe = daoClasse.getVos().firstElement();
                System.out.println(">>>>" + classe.getNameClasse());
                daoRace.selectByPK(classe.getRaceId());
                Race race = daoRace.getVos().firstElement();
                System.out.println(">>>>" + race.getRace());
                daomap.selectByPK(player.getMapId());
                map = daomap.getVos().firstElement();
                String msg = "loadPlayer" + "/" + player.getId() + "/" + player.getName() + "/" + player.getLoginId() + "/" + player.getMapId() + "/" + player.getClasseId() + "/" + player.getHpMax() + "/" + player.getHpCurr() + "/" + player.getManaMax() + "/" + player.getManaCurr() + "/" + player.getExpMax() + "/" + player.getExpCurr() + "/" + player.getSp() + "/" + player.getStr() + "/" + player.getDex() + "/" + player.getInte() + "/" + player.getCon() + "/" + player.getCha() + "/" + player.getWis() + "/" + player.getStamina() + "/" + player.getSex() + "/" + player.getResMagic() + "/" + player.getResPhysical() + "/" + player.getEvasion() + "/" + player.getDateCreate() + "/" + player.getOnLine() + "/" + player.getLastAcess() + "/" + player.getSector() + "/" + classe.getNameClasse() + "/" + race.getRace() + "/" + map.getStartTileHeroPosX() + "/" + map.getStartTileHeroPosY() + "/" + map.getPosition() + "/";
                System.out.println(msg);
                session.send(encodeString(msg));
                Channel channel = AppContext.getChannelManager().getChannel("map_" + player.getMapId());
                channel.send(null, encodeString("m/" + player.getLoginId() + "/" + player.getClasseId() + "/" + player.getName() + "/" + map.getStartTileHeroPosX() + "/" + map.getStartTileHeroPosY() + "/" + map.getPosition() + "/"));
                SessionPlayer = ManagerSessionPlayer.loggedIn(session);
                SessionPlayer.enter(getRoom("Room" + player.getMapId()), player);
                AppContext.getChannelManager().getChannel("map_" + player.getMapId()).join(session);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SessionPlayer;
    }
}
