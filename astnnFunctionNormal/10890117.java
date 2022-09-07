class BackupThread extends Thread {
    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int mode = slea.readByte();
        MapleCharacter player = c.getPlayer();
        WorldChannelInterface worldInterface = c.getChannelServer().getWorldInterface();
        BuddyList buddylist = player.getBuddylist();
        if (mode == 1) {
            String addName = slea.readMapleAsciiString();
            BuddylistEntry ble = buddylist.get(addName);
            MapleCharacter addee = null;
            if (loadCharacterFromDB(addName, c) != null) {
                addee = loadCharacterFromDB(addName, c);
            }
            if (addee != null) {
                if (addee.isGM()) {
                    if (player.isGM()) {
                    } else {
                        c.getSession().write(MaplePacketCreator.buddylistMessage((byte) 14));
                        return;
                    }
                } else {
                    if (player.isGM()) {
                        c.getSession().write(MaplePacketCreator.serverNotice(1, "Adding of non-GMs is not allowed."));
                        c.getSession().write(MaplePacketCreator.enableActions());
                        return;
                    } else {
                    }
                }
                if (ble != null && !ble.isVisible()) {
                    c.getSession().write(MaplePacketCreator.buddylistMessage((byte) 13));
                    return;
                } else if (buddylist.isFull()) {
                    c.getSession().write(MaplePacketCreator.buddylistMessage((byte) 11));
                    return;
                } else {
                    try {
                        CharacterIdNameBuddyCapacity charWithId = null;
                        int channel;
                        MapleCharacter otherChar = c.getChannelServer().getPlayerStorage().getCharacterByName(addName);
                        if (otherChar != null) {
                            channel = c.getChannel();
                            charWithId = new CharacterIdNameBuddyCapacity(otherChar.getId(), otherChar.getName(), otherChar.getBuddylist().getCapacity());
                        } else {
                            channel = worldInterface.find(addName);
                            charWithId = getCharacterIdAndNameFromDatabase(addName);
                        }
                        if (charWithId != null) {
                            BuddyAddResult buddyAddResult = null;
                            if (channel != -1) {
                                ChannelWorldInterface channelInterface = worldInterface.getChannelInterface(channel);
                                buddyAddResult = channelInterface.requestBuddyAdd(addName, c.getChannel(), player.getId(), player.getName());
                            } else {
                                Connection con = DatabaseConnection.getConnection();
                                PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) as buddyCount FROM buddies WHERE characterid = ? AND pending = 0");
                                ps.setInt(1, charWithId.getId());
                                ResultSet rs = ps.executeQuery();
                                if (!rs.next()) {
                                    throw new RuntimeException("Result set expected");
                                } else {
                                    int count = rs.getInt("buddyCount");
                                    if (count >= charWithId.getBuddyCapacity()) {
                                        buddyAddResult = BuddyAddResult.BUDDYLIST_FULL;
                                    }
                                }
                                rs.close();
                                ps.close();
                                ps = con.prepareStatement("SELECT pending FROM buddies WHERE characterid = ? AND buddyid = ?");
                                ps.setInt(1, charWithId.getId());
                                ps.setInt(2, player.getId());
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    buddyAddResult = BuddyAddResult.ALREADY_ON_LIST;
                                }
                                rs.close();
                                ps.close();
                            }
                            if (buddyAddResult == BuddyAddResult.BUDDYLIST_FULL) {
                                c.getSession().write(MaplePacketCreator.buddylistMessage((byte) 12));
                            } else {
                                int displayChannel = -1;
                                int otherCid = charWithId.getId();
                                if (buddyAddResult == BuddyAddResult.ALREADY_ON_LIST && channel != -1) {
                                    displayChannel = channel;
                                    notifyRemoteChannel(c, channel, otherCid, ADDED);
                                } else if (buddyAddResult != BuddyAddResult.ALREADY_ON_LIST && channel == -1) {
                                    Connection con = DatabaseConnection.getConnection();
                                    PreparedStatement ps = con.prepareStatement("INSERT INTO buddies (characterid, `buddyid`, `pending`) VALUES (?, ?, 1)");
                                    ps.setInt(1, charWithId.getId());
                                    ps.setInt(2, player.getId());
                                    ps.executeUpdate();
                                    ps.close();
                                }
                                buddylist.put(new BuddylistEntry(charWithId.getName(), otherCid, displayChannel, true));
                                c.getSession().write(MaplePacketCreator.updateBuddylist(buddylist.getBuddies()));
                            }
                        } else {
                            c.getSession().write(MaplePacketCreator.buddylistMessage((byte) 15));
                        }
                    } catch (RemoteException e) {
                        log.error("REMOTE THROW", e);
                    } catch (SQLException e) {
                        log.error("SQL THROW", e);
                    }
                }
            } else {
                c.getSession().write(MaplePacketCreator.buddylistMessage((byte) 15));
            }
        } else if (mode == 2) {
            int otherCid = slea.readInt();
            if (!buddylist.isFull()) {
                try {
                    int channel = worldInterface.find(otherCid);
                    String otherName = null;
                    MapleCharacter otherChar = c.getChannelServer().getPlayerStorage().getCharacterById(otherCid);
                    if (otherChar == null) {
                        Connection con = DatabaseConnection.getConnection();
                        PreparedStatement ps = con.prepareStatement("SELECT name FROM characters WHERE id = ?");
                        ps.setInt(1, otherCid);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            otherName = rs.getString("name");
                        }
                        rs.close();
                        ps.close();
                    } else {
                        otherName = otherChar.getName();
                    }
                    if (otherName != null) {
                        buddylist.put(new BuddylistEntry(otherName, otherCid, channel, true));
                        c.getSession().write(MaplePacketCreator.updateBuddylist(buddylist.getBuddies()));
                        notifyRemoteChannel(c, channel, otherCid, ADDED);
                    }
                } catch (RemoteException e) {
                    log.error("REMOTE THROW", e);
                } catch (SQLException e) {
                    log.error("SQL THROW", e);
                }
            }
            nextPendingRequest(c);
        } else if (mode == 3) {
            int otherCid = slea.readInt();
            if (buddylist.containsVisible(otherCid)) {
                try {
                    notifyRemoteChannel(c, worldInterface.find(otherCid), otherCid, DELETED);
                } catch (RemoteException e) {
                    log.error("REMOTE THROW", e);
                }
            }
            buddylist.remove(otherCid);
            c.getSession().write(MaplePacketCreator.updateBuddylist(player.getBuddylist().getBuddies()));
            nextPendingRequest(c);
        }
    }
}
