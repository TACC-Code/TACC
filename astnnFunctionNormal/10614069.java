class BackupThread extends Thread {
    public static void executeCommand(Connection con, String[] data) throws SQLException, MessagingException, VerificationException {
        Player thisPlayer = con.getPlayer();
        if (data[0].equals(Protocol.HEARTBEAT)) {
            Logger.logInTraffic(data, con);
            return;
        }
        try {
            ClientProtocolCommands command = null;
            try {
                command = ClientProtocolCommands.values()[Integer.parseInt(data[0])];
            } catch (Exception e) {
                Logger.logInTraffic(data, con);
                TaskProcesser.addTask(new Task(TaskTypes.LOGOFF, con, new String[] {}));
                String exceptionString = "Client sent unknown command!" + "\n\tData (length=" + data.length + "):";
                for (int i = 0; i < data.length; i++) {
                    exceptionString += "\n\t\t" + i + ": " + data[i];
                }
                throw new VerificationException(exceptionString);
            }
            String[] information = new String[data.length - 1];
            System.arraycopy(data, 1, information, 0, information.length);
            Logger.logInTraffic(command, information, con);
            if (thisPlayer == null) {
                switch(command) {
                    case NEW_PLAYER:
                        CommandMethods.createPlayer(con, information);
                        return;
                    case LOGIN:
                        CommandMethods.login(con, information);
                        return;
                    case QUIT:
                        TaskProcesser.addTask(new Task(TaskTypes.LOGOFF, con, new String[] {}));
                        return;
                    case PASSWORD_RECOVERY:
                        String[] playerData = Database.getPlayerDataByName(information[0]);
                        if (playerData == null) {
                            return;
                        }
                        String email = playerData[4];
                        if (email.equals(information[1])) {
                            String newPassword = getRandomString(10);
                            long pid = Long.parseLong(playerData[0]);
                            Database.updatePassword(pid, PasswordEncrypter.encryptPassword(newPassword));
                            String loginname = playerData[1];
                            MailSender.sendPasswordRecoveryMail(loginname, email, newPassword);
                        }
                        return;
                    case CLIENTVERSION:
                        con.setClientVersion(information[0]);
                        Protocol.sendIP(con, con.getIpAddress());
                        break;
                    case CONNECTION_TEST_REQUEST:
                        CommandMethods.testConnection(information);
                        break;
                    default:
                        TaskProcesser.addTask(new Task(TaskTypes.LOGOFF, con, new String[] {}));
                }
            } else {
                switch(command) {
                    case CLIENTVERSION:
                        con.setClientVersion(information[0]);
                        return;
                    case LOGIN:
                        return;
                    case JOIN_CHANNEL:
                        Channel requestedchannel = null;
                        requestedchannel = ChannelData.getChannel(information[0]);
                        if (requestedchannel == null) {
                            requestedchannel = new Channel(information[0]);
                            ChannelData.addChannel(requestedchannel);
                        }
                        if (!(Arrays.asList(requestedchannel.getPlayersInChannel()).contains(thisPlayer))) {
                            thisPlayer.joinChannel(requestedchannel);
                            CommandMethods.refreshRoomsAndPlayers(thisPlayer, requestedchannel);
                        }
                        return;
                    case QUIT:
                        TaskProcesser.addTask(new Task(TaskTypes.LOGOFF, con, new String[] {}));
                        return;
                    case LEAVE_CHANNEL:
                        Channel currentChannel = ChannelData.getChannel(information[0]);
                        if (currentChannel != null) {
                            currentChannel.removePlayer(thisPlayer);
                        }
                        return;
                    case CHAT_MAIN:
                        if (thisPlayer.getSleepMode() == true) {
                            thisPlayer.setSleepMode(false);
                        }
                        thisPlayer.setAway(false);
                        Protocol.mainChat(ChannelData.getChannel(information[0]), thisPlayer, information[1]);
                        return;
                    case CHAT_ROOM:
                        if (thisPlayer.getCurrentRoom() != null) {
                            Protocol.roomChat(thisPlayer.getCurrentRoom(), thisPlayer, information[0]);
                            thisPlayer.setAway(false);
                        }
                        return;
                    case NUDGE:
                        Player nudgeTo = PlayerData.searchByName(information[0]);
                        if (nudgeTo != null) {
                            Protocol.nudge(nudgeTo.getConnection(), thisPlayer);
                            thisPlayer.setAway(false);
                        }
                        return;
                    case WHISPER:
                        Player whisperTo = PlayerData.searchByName(information[0]);
                        if (whisperTo != null) {
                            Protocol.privateMessage(whisperTo.getConnection(), thisPlayer, information[1]);
                        } else {
                            Protocol.sendWhisperToOfflineUser(thisPlayer, information[0]);
                        }
                        thisPlayer.setAway(false);
                        return;
                    case KICK:
                        Player kickedone = PlayerData.searchByName(information[0]);
                        Player kicker = thisPlayer;
                        if (kickedone != null && kickedone.getCurrentRoom() != null && kicker != null && kicker.equals(kickedone.getCurrentRoom().getHost()) && !kickedone.equals(kickedone.getCurrentRoom().getHost())) {
                            Protocol.kick(kickedone.getConnection());
                            kickedone.getCurrentRoom().removePlayer(kickedone);
                            kickedone.setCurrentRoom(null);
                            Protocol.roomChat(kicker.getCurrentRoom(), null, kickedone.getLoginName() + " has been kicked!");
                        }
                        return;
                    case BAN:
                        Player bannedone = PlayerData.searchByName(information[0]);
                        if (bannedone != null && bannedone != thisPlayer) {
                            thisPlayer.ban(bannedone.getPid());
                        } else {
                            thisPlayer.ban(Database.getPID(information[0]));
                        }
                        Protocol.confirmBan(thisPlayer.getConnection(), information[0]);
                        return;
                    case UNBAN:
                        Player unbannedone = PlayerData.searchByName(information[0]);
                        if (unbannedone != null) {
                            thisPlayer.unBan(unbannedone.getPid());
                        } else {
                            thisPlayer.unBan(Database.getPID(information[0]));
                        }
                        Protocol.confirmUnBan(thisPlayer.getConnection(), information[0]);
                        return;
                    case MUTE:
                        Player mutedone = PlayerData.searchByName(information[0]);
                        if (mutedone != null) {
                            thisPlayer.mute(mutedone.getPid());
                        } else {
                            thisPlayer.mute(Database.getPID(information[0]));
                        }
                        Protocol.confirmMute(thisPlayer.getConnection(), information[0]);
                        return;
                    case UNMUTE:
                        Player unmutedone = PlayerData.searchByName(information[0]);
                        if (unmutedone != null) {
                            thisPlayer.unMute(unmutedone.getPid());
                        } else {
                            thisPlayer.unMute(Database.getPID(information[0]));
                        }
                        Protocol.confirmUnMute(thisPlayer.getConnection(), information[0]);
                        return;
                    case CREATE_ROOM:
                        CommandMethods.createRoom(thisPlayer, information);
                        return;
                    case JOIN_ROOM:
                        String hostName = information[0];
                        Player host = PlayerData.searchByName(hostName);
                        if (host != null && host.getCurrentRoom() != null) {
                            String password = information.length == 2 ? information[1] : "";
                            CommandMethods.joinRoom(thisPlayer, host.getCurrentRoom(), password);
                        } else {
                            Protocol.errorRoomDoesNotExist(thisPlayer.getConnection());
                        }
                        return;
                    case JOIN_ROOM_BY_ID:
                        Room roomByID = RoomData.getRoomByID(Long.valueOf(information[0]));
                        if (roomByID == null) {
                            Protocol.errorRoomDoesNotExist(thisPlayer.getConnection());
                        } else {
                            String password = information.length == 2 ? information[1] : "";
                            if (roomByID.isPasswordProtected() && password.length() == 0) {
                                Protocol.requestPassword(thisPlayer.getConnection(), information[0]);
                                return;
                            } else {
                                CommandMethods.joinRoom(thisPlayer, roomByID, password);
                            }
                        }
                        return;
                    case LEAVE_ROOM:
                        if (thisPlayer.getCurrentRoom() != null) {
                            thisPlayer.getCurrentRoom().removePlayer(thisPlayer);
                            thisPlayer.setAway(false);
                        }
                        return;
                    case CLOSE_ROOM:
                        if (thisPlayer.getCurrentRoom() != null) {
                            thisPlayer.getCurrentRoom().close();
                            thisPlayer.setAway(false);
                        }
                        return;
                    case GAME_CLOSED:
                        Channel ch = ChannelData.getChannel(information[0]);
                        Player[] recipients = null;
                        if (ch != null) {
                            recipients = ch.getPlayersInChannel();
                        } else if (thisPlayer.getCurrentRoom() != null) {
                            recipients = thisPlayer.getCurrentRoom().getPlayers();
                        } else {
                            recipients = new Player[0];
                        }
                        Protocol.gameClosed(information[0], thisPlayer, recipients);
                        thisPlayer.setPlaying(false);
                        thisPlayer.setSleepMode(false);
                        if (thisPlayer.getCurrentRoom() != null && thisPlayer.getCurrentRoom().isInstantLaunched()) {
                            thisPlayer.getCurrentRoom().removePlayer(thisPlayer);
                        }
                        thisPlayer.sendMyContactStatusToMyContacts();
                        return;
                    case REFRESH_ROOMS_AND_PLAYERS:
                        CommandMethods.refreshRoomsAndPlayers(thisPlayer, ChannelData.getChannel(information[0]));
                        return;
                    case SET_GAMESETTING:
                        if (thisPlayer.getCurrentRoom() != null && thisPlayer.getCurrentRoom().getHost().equals(thisPlayer)) {
                            thisPlayer.getCurrentRoom().setSetting(information[0], information[1]);
                        }
                        return;
                    case FLIP_READY:
                        thisPlayer.flipReady();
                        return;
                    case LAUNCH:
                        Room current = thisPlayer.getCurrentRoom();
                        if (current != null && current.getHost().equals(thisPlayer)) {
                            current.launch();
                        }
                        return;
                    case EDIT_PROFILE:
                        Protocol.editProfile(thisPlayer);
                        return;
                    case REQUEST_PROFILE:
                        Player viewed = PlayerData.searchByName(information[0]);
                        if (viewed == null) {
                            String[] playerData = Database.getPlayerDataByName(information[0]);
                            Protocol.showProfile(thisPlayer.getConnection(), playerData[1], playerData[2], playerData[6], playerData[5]);
                        } else {
                            Protocol.showProfile(thisPlayer.getConnection(), viewed.getLoginName(), viewed.getIngameName(), viewed.getCountry(), viewed.getWebsite());
                        }
                        return;
                    case SAVE_PROFILE:
                        CommandMethods.saveProfile(con, thisPlayer, information);
                        return;
                    case SEND_FILE:
                        Player reciever = PlayerData.searchByName(information[0]);
                        if (reciever != null) {
                            Protocol.sendFile(reciever.getConnection(), thisPlayer, information[1], information[2], con.getIpAddress(), information[3]);
                        }
                        return;
                    case ACCEPT_FILE:
                        Player recv = PlayerData.searchByName(information[0]);
                        if (recv != null) {
                            Protocol.acceptFile(recv.getConnection(), thisPlayer, information[1], con.getIpAddress(), information[2], information[3]);
                        }
                        return;
                    case REFUSE_FILE:
                        Player recvv = PlayerData.searchByName(information[0]);
                        if (recvv != null) {
                            Protocol.refuseFile(recvv.getConnection(), thisPlayer, information[1]);
                        }
                        return;
                    case CANCEL_FILE:
                        Player recvvv = PlayerData.searchByName(information[0]);
                        if (recvvv != null) {
                            Protocol.cancelFile(recvvv.getConnection(), thisPlayer, information[1]);
                        }
                        return;
                    case TURN_AROUND_FILE:
                        Player turned = PlayerData.searchByName(information[0]);
                        if (turned != null) {
                            Protocol.turnAroundTransfer(turned.getConnection(), thisPlayer, information[1]);
                        }
                        return;
                    case SET_SLEEP:
                        thisPlayer.setSleepModeEnabled(Boolean.valueOf(information[0]));
                        return;
                    case CHANGE_PASSWORD:
                        if (!Verification.verifyPassword(information[1])) {
                            throw new VerificationException("Failure on password! " + information[1]);
                        }
                        if (thisPlayer.getPassword().equals(information[0])) {
                            thisPlayer.setPassword(information[1]);
                            Protocol.confirmPasswordChange(con);
                        } else {
                            Protocol.errorIncorrectPassword(con);
                        }
                        return;
                    case INVITE_USER:
                        Player target = PlayerData.searchByName(information[0]);
                        if (target == null) {
                            Protocol.sendWhisperToOfflineUser(thisPlayer, information[0]);
                            return;
                        }
                        if (thisPlayer.getCurrentRoom() != null) {
                            Protocol.inviteUser(target.getConnection(), thisPlayer, thisPlayer.getCurrentRoom());
                        }
                        return;
                    case SEND_CONTACT_REQUEST:
                        Player sendTo = PlayerData.searchByName(information[0]);
                        long sendToID;
                        if (sendTo != null) {
                            sendToID = sendTo.getPid();
                        } else {
                            sendToID = Long.valueOf(Database.getPlayerDataByName(information[0])[0]);
                        }
                        synchronized (thisPlayer.getContactList()) {
                            if (!thisPlayer.getContactList().isOnMyList(sendToID)) {
                                if (thisPlayer.getContactList().addPendingContact(sendToID)) {
                                    if (sendTo != null) {
                                        Protocol.sendContactRequest(sendTo.getConnection(), thisPlayer);
                                        sendTo.getContactList().addRequest(thisPlayer.getPid());
                                    }
                                    Protocol.acknowledgeContactRequest(con, null, information[0]);
                                }
                            }
                        }
                        return;
                    case ACCEPT_CONTACT_REQUEST:
                        Player accepted = PlayerData.searchByName(information[0]);
                        synchronized (thisPlayer.getContactList()) {
                            if (accepted != null) {
                                accepted.getContactList().contactAcceptedRequest(thisPlayer.getPid());
                                thisPlayer.getContactList().acceptRequest(accepted.getPid());
                                Protocol.sendRequestAcceptNotification(accepted.getConnection(), thisPlayer);
                                Protocol.sendContactStatus(accepted.getConnection(), thisPlayer, thisPlayer.getContactStatus());
                                Protocol.acknowledgeContactAccept(con, null, accepted.getLoginName());
                            } else {
                                Long ID = Long.valueOf(Database.getPlayerDataByName(information[0])[0]);
                                Database.setContactAccepted(ID, thisPlayer.getPid());
                                thisPlayer.getContactList().acceptRequest(ID);
                                Protocol.acknowledgeContactAccept(con, null, Database.getLoginName(ID));
                            }
                        }
                        return;
                    case REFUSE_CONTACT_REQUEST:
                        Player refusedContact = PlayerData.searchByName(information[0]);
                        synchronized (thisPlayer.getContactList()) {
                            if (refusedContact != null) {
                                thisPlayer.getContactList().refuseRequest(refusedContact.getPid());
                                refusedContact.getContactList().contactRefusedRequest(thisPlayer.getPid());
                                Protocol.sendRequestRefuseNotification(refusedContact.getConnection(), thisPlayer);
                                Protocol.acknowledgeContactRefuse(con, null, refusedContact.getLoginName());
                            } else {
                                Long ID = Long.valueOf(Database.getPlayerDataByName(information[0])[0]);
                                thisPlayer.getContactList().refuseRequest(ID);
                                Database.removeContact(ID, thisPlayer.getPid());
                                Protocol.acknowledgeContactRefuse(con, null, Database.getLoginName(ID));
                            }
                        }
                        return;
                    case CREATE_GROUP:
                        if (!Verification.verifyGroupName(information[0])) {
                            throw new VerificationException("Failure on groupname! " + information[0]);
                        }
                        if (thisPlayer.getContactList().groupExists(information[0])) {
                            throw new VerificationException("User wanted to create a group with a groupname that already exists! " + information[0]);
                        }
                        synchronized (thisPlayer.getContactList()) {
                            thisPlayer.getContactList().createGroup(information[0]);
                            Protocol.acknowledgeGroupCreate(con, null, information[0]);
                        }
                        return;
                    case RENAME_GROUP:
                        if (!Verification.verifyGroupName(information[1])) {
                            throw new VerificationException("Failure on groupname! " + information[1]);
                        }
                        synchronized (thisPlayer.getContactList()) {
                            thisPlayer.getContactList().renameGroup(information[0], information[1]);
                            Protocol.acknowledgeGroupRename(con, null, information[0], information[1]);
                        }
                        return;
                    case DELETE_CONTACT:
                        Player contact = PlayerData.searchByName(information[0]);
                        synchronized (thisPlayer.getContactList()) {
                            if (contact != null) {
                                Long ID = contact.getPid();
                                thisPlayer.getContactList().removeContact(ID);
                                contact.getContactList().removeContactWhoKnowsMe(thisPlayer.getPid());
                            } else {
                                Long ID = Long.valueOf(Database.getPlayerDataByName(information[0])[0]);
                                thisPlayer.getContactList().removeContact(ID);
                            }
                        }
                        Protocol.acknowledgeContactRemove(con, null, information[0]);
                        return;
                    case DELETE_GROUP:
                        synchronized (thisPlayer.getContactList()) {
                            thisPlayer.getContactList().deleteGroup(information[0]);
                            Protocol.acknowledgeGroupDelete(con, null, information[0]);
                        }
                        return;
                    case MOVE_TO_GROUP:
                        Player moved = PlayerData.searchByName(information[0]);
                        synchronized (thisPlayer.getContactList()) {
                            if (moved != null) {
                                thisPlayer.getContactList().moveContact(moved.getPid(), information[1]);
                            } else {
                                Long ID = Long.valueOf(Database.getPlayerDataByName(information[0])[0]);
                                thisPlayer.getContactList().moveContact(ID, information[1]);
                            }
                        }
                        Protocol.acknowledgeContactMove(con, null, information[0], information[1]);
                        return;
                    case REFRESH_CONTACTS:
                        synchronized (thisPlayer.getContactList()) {
                            Protocol.sendContactData(thisPlayer, Boolean.parseBoolean(information[0]));
                        }
                        return;
                    case SETAWAYSTATUS:
                        thisPlayer.setAway(true);
                        break;
                    case UNSETAWAYSTATUS:
                        thisPlayer.setAway(false);
                        break;
                    case CONNECTION_TEST_REQUEST:
                        if (thisPlayer.getCurrentRoom() == null) {
                            CommandMethods.testConnection(information);
                        } else {
                            Protocol.sendConnectionTestRequest(thisPlayer.getCurrentRoom().getHost(), information);
                        }
                        break;
                    default:
                        TaskProcesser.addTask(new Task(TaskTypes.LOGOFF, con, new String[] {}));
                        String exceptionString = "The command enum has a value that was not handled! " + command.toString() + "\n\tData (length=" + data.length + "):";
                        for (int i = 0; i < data.length; i++) {
                            exceptionString += "\n\t\t" + i + ": " + data[i];
                        }
                        throw new VerificationException(exceptionString);
                }
            }
        } catch (ArrayIndexOutOfBoundsException exc) {
            Logger.log(exc, con);
            TaskProcesser.addTask(new Task(TaskTypes.LOGOFF, con, new String[] {}));
            throw new VerificationException("Player sent command with missing information!");
        } catch (SQLException sqlexc) {
            Protocol.sendCrippledModeNotification(con);
            Logger.log(sqlexc, con);
            return;
        }
    }
}
