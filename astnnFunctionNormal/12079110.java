class BackupThread extends Thread {
    @Override
    protected void handleSnacPacket(SnacPacketEvent e) {
        Log.debug("OSCAR snac packet received: " + e);
        SnacCommand cmd = e.getSnacCommand();
        if (cmd instanceof ServerReadyCmd) {
            ServerReadyCmd src = (ServerReadyCmd) cmd;
            setSnacFamilies(src.getSnacFamilies());
            Collection<SnacFamilyInfo> familyInfos = SnacFamilyInfoFactory.getDefaultFamilyInfos(src.getSnacFamilies());
            setSnacFamilyInfos(familyInfos);
            getMainSession().registerSnacFamilies(this);
            request(new ClientVersionsCmd(familyInfos));
            request(new RateInfoRequest());
        } else if (cmd instanceof RecvImIcbm) {
            RecvImIcbm icbm = (RecvImIcbm) cmd;
            String sn = icbm.getSenderInfo().getScreenname();
            InstantMessage message = icbm.getMessage();
            String msg = StringUtils.convertFromHtml(message.getMessage());
            getMainSession().getTransport().sendMessage(getMainSession().getJID(), getMainSession().getTransport().convertIDToJID(sn), msg);
        } else if (cmd instanceof OldIcbm) {
            OldIcbm oicbm = (OldIcbm) cmd;
            if (oicbm.getMessageType() == OldIcbm.MTYPE_PLAIN) {
                String uin = String.valueOf(oicbm.getSender());
                String msg = StringUtils.convertFromHtml(oicbm.getReason());
                Log.debug("Got ICBM message " + uin + " with " + msg + "\n" + oicbm);
                getMainSession().getTransport().sendMessage(getMainSession().getJID(), getMainSession().getTransport().convertIDToJID(uin), msg);
            }
        } else if (cmd instanceof WarningNotification) {
            WarningNotification wn = (WarningNotification) cmd;
            MiniUserInfo warner = wn.getWarner();
            if (warner == null) {
                getMainSession().getTransport().sendMessage(getMainSession().getJID(), getMainSession().getTransport().getJID(), LocaleUtils.getLocalizedString("gateway.aim.warninganon", "kraken", Arrays.asList(wn.getNewLevel().toString())), Message.Type.headline);
            } else {
                Log.debug("*** " + warner.getScreenname() + " warned you up to " + wn.getNewLevel() + "%");
                getMainSession().getTransport().sendMessage(getMainSession().getJID(), getMainSession().getTransport().getJID(), LocaleUtils.getLocalizedString("gateway.aim.warningdirect", "kraken", Arrays.asList(warner.getScreenname(), wn.getNewLevel().toString())), Message.Type.headline);
            }
        } else if (cmd instanceof ExtraInfoAck) {
            ExtraInfoAck eia = (ExtraInfoAck) cmd;
            List<ExtraInfoBlock> extraInfo = eia.getExtraInfos();
            if (extraInfo != null) {
                for (ExtraInfoBlock i : extraInfo) {
                    ExtraInfoData data = i.getExtraData();
                    final byte[] pendingAvatar = getMainSession().getSsiHierarchy().getPendingAvatarData();
                    if (JiveGlobals.getBooleanProperty("plugin.gateway." + getMainSession().getTransport().getType() + ".avatars", true) && (data.getFlags() & ExtraInfoData.FLAG_UPLOAD_ICON) != 0 && pendingAvatar != null) {
                        Log.debug("OSCAR: Server has indicated that it wants our icon.");
                        request(new UploadIconCmd(ByteBlock.wrap(pendingAvatar)), new SnacRequestAdapter() {

                            @Override
                            public void handleResponse(SnacResponseEvent e) {
                                SnacCommand cmd = e.getSnacCommand();
                                if (cmd instanceof UploadIconAck && pendingAvatar != null) {
                                    UploadIconAck iconAck = (UploadIconAck) cmd;
                                    if (iconAck.getCode() == UploadIconAck.CODE_DEFAULT || iconAck.getCode() == UploadIconAck.CODE_SUCCESS) {
                                        ExtraInfoBlock iconInfo = iconAck.getIconInfo();
                                        if (iconInfo == null) {
                                            Log.debug("OSCAR: Got icon ack with no iconInfo: " + iconAck);
                                        }
                                        Log.debug("OSCAR: Successfully set icon.");
                                        try {
                                            MessageDigest md = MessageDigest.getInstance("MD5");
                                            md.update(pendingAvatar);
                                            getMainSession().getAvatar().setLegacyIdentifier(org.jivesoftware.util.StringUtils.encodeHex(md.digest()));
                                        } catch (NoSuchAlgorithmException ee) {
                                            Log.error("No algorithm found for MD5!", ee);
                                        }
                                    } else if (iconAck.getCode() == UploadIconAck.CODE_BAD_FORMAT) {
                                        Log.debug("OSCAR: Uploaded icon was not in an unaccepted format.");
                                    } else if (iconAck.getCode() == UploadIconAck.CODE_TOO_LARGE) {
                                        Log.debug("OSCAR: Uploaded icon was too large to be accepted.");
                                    } else {
                                        Log.debug("OSCAR: Got unknown code from UploadIconAck: " + iconAck.getCode());
                                    }
                                } else if (cmd instanceof SnacError) {
                                    Log.debug("Got SnacError while setting icon: " + cmd);
                                }
                                getMainSession().getSsiHierarchy().clearPendingAvatar();
                            }
                        });
                    }
                }
            }
        } else if (cmd instanceof BuddyStatusCmd) {
            BuddyStatusCmd bsc = (BuddyStatusCmd) cmd;
            FullUserInfo info = bsc.getUserInfo();
            PresenceType pType = PresenceType.available;
            String vStatus = "";
            if (info.getAwayStatus()) {
                pType = PresenceType.away;
            }
            if ((info.getFlags() & FullUserInfo.MASK_WIRELESS) != 0) {
                pType = PresenceType.xa;
                vStatus = "Mobile: ";
            }
            if (getMainSession().getTransport().getType().equals(TransportType.icq) && info.getScreenname().matches("/^\\d+$/")) {
                pType = ((OSCARTransport) getMainSession().getTransport()).convertICQStatusToXMPP(info.getIcqStatus());
            }
            List<ExtraInfoBlock> extraInfo = info.getExtraInfoBlocks();
            if (extraInfo != null) {
                for (ExtraInfoBlock i : extraInfo) {
                    ExtraInfoData data = i.getExtraData();
                    if (i.getType() == ExtraInfoBlock.TYPE_AVAILMSG) {
                        ByteBlock msgBlock = data.getData();
                        int len = BinaryTools.getUShort(msgBlock, 0);
                        if (len >= 0) {
                            byte[] msgBytes = msgBlock.subBlock(2, len).toByteArray();
                            String msg;
                            try {
                                msg = new String(msgBytes, "UTF-8");
                            } catch (UnsupportedEncodingException e1) {
                                continue;
                            }
                            if (msg.length() > 0) {
                                vStatus = vStatus + msg;
                            }
                        }
                    } else if (i.getType() == ExtraInfoBlock.TYPE_ICONHASH && JiveGlobals.getBooleanProperty("plugin.gateway." + getMainSession().getTransport().getType() + ".avatars", true)) {
                        try {
                            OSCARBuddy oscarBuddy = getMainSession().getBuddyManager().getBuddy(getMainSession().getTransport().convertIDToJID(info.getScreenname()));
                            Avatar curAvatar = oscarBuddy.getAvatar();
                            if (curAvatar == null || !curAvatar.getLegacyIdentifier().equals(org.jivesoftware.util.StringUtils.encodeHex(i.getExtraData().getData().toByteArray()))) {
                                IconRequest req = new IconRequest(info.getScreenname(), i.getExtraData());
                                request(req, new SnacRequestAdapter() {

                                    @Override
                                    public void handleResponse(SnacResponseEvent e) {
                                        SnacCommand cmd = e.getSnacCommand();
                                        if (cmd instanceof IconDataCmd) {
                                            IconDataCmd idc = (IconDataCmd) cmd;
                                            if (idc.getIconData().getLength() > 0 && idc.getIconData().getLength() != 90) {
                                                Log.debug("Got icon data: " + idc);
                                                if (getMainSession().getBuddyManager().isActivated()) {
                                                    try {
                                                        OSCARBuddy oscarBuddy = getMainSession().getBuddyManager().getBuddy(getMainSession().getTransport().convertIDToJID(idc.getScreenname()));
                                                        oscarBuddy.setAvatar(new Avatar(getMainSession().getTransport().convertIDToJID(idc.getScreenname()), org.jivesoftware.util.StringUtils.encodeHex(idc.getIconInfo().getExtraData().getData().toByteArray()), idc.getIconData().toByteArray()));
                                                    } catch (NotFoundException ee) {
                                                    } catch (IllegalArgumentException ee) {
                                                        Log.debug("OSCAR: Got null avatar, ignoring.");
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void handleTimeout(SnacRequestTimeoutEvent e) {
                                        Log.debug("Time out while waiting for icon data.");
                                    }
                                });
                            }
                        } catch (NotFoundException ee) {
                        }
                    }
                }
            }
            if (getMainSession().getBuddyManager().isActivated()) {
                try {
                    OSCARBuddy oscarBuddy = getMainSession().getBuddyManager().getBuddy(getMainSession().getTransport().convertIDToJID(info.getScreenname()));
                    oscarBuddy.setPresenceAndStatus(pType, vStatus);
                } catch (NotFoundException ee) {
                    Log.debug("OSCAR: Received presense notification for contact we don't care about: " + info.getScreenname());
                }
            } else {
                getMainSession().getBuddyManager().storePendingStatus(getMainSession().getTransport().convertIDToJID(info.getScreenname()), pType, vStatus);
            }
        } else if (cmd instanceof BuddyOfflineCmd) {
            BuddyOfflineCmd boc = (BuddyOfflineCmd) cmd;
            if (getMainSession().getBuddyManager().isActivated()) {
                try {
                    OSCARBuddy oscarBuddy = getMainSession().getBuddyManager().getBuddy(getMainSession().getTransport().convertIDToJID(boc.getScreenname()));
                    oscarBuddy.setPresence(PresenceType.unavailable);
                } catch (NotFoundException ee) {
                }
            } else {
                getMainSession().getBuddyManager().storePendingStatus(getMainSession().getTransport().convertIDToJID(boc.getScreenname()), PresenceType.unavailable, null);
            }
        } else if (cmd instanceof TypingCmd) {
            TypingCmd tc = (TypingCmd) cmd;
            String sn = tc.getScreenname();
            final ChatStateEventSource chatStateEventSource = getMainSession().getTransport().getChatStateEventSource();
            final JID receiver = getMainSession().getJID();
            final JID sender = getMainSession().getTransport().convertIDToJID(sn);
            if (tc.getTypingState() == TypingCmd.STATE_TYPING) {
                chatStateEventSource.isComposing(sender, receiver);
            } else if (tc.getTypingState() == TypingCmd.STATE_PAUSED) {
                chatStateEventSource.sendIsPaused(sender, receiver);
            } else if (tc.getTypingState() == TypingCmd.STATE_NO_TEXT) {
                chatStateEventSource.isInactive(sender, receiver);
            }
        }
    }
}
