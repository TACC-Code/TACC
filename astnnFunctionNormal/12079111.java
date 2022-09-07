class BackupThread extends Thread {
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
}
