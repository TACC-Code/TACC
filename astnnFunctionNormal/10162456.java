class BackupThread extends Thread {
    public Channels toChannels() {
        Channels channels = new Channels();
        InputTable channelTable = getTableNamed("CHANNEL");
        if (channelTable == null) {
            return channels;
        }
        int nchannels = channelTable.getValues().size();
        for (int i = 0; i < nchannels; i++) {
            try {
                Channel channel = new Channel();
                channel.setId(channelTable.getValue(i, "CHAN_NO"));
                channel.setLength((int) Double.parseDouble(channelTable.getValue(i, "LENGTH")));
                channel.setMannings(Double.parseDouble(channelTable.getValue(i, "MANNING")));
                channel.setDispersion(Double.parseDouble(channelTable.getValue(i, "DISPERSION")));
                channel.setUpNodeId(channelTable.getValue(i, "UPNODE"));
                channel.setDownNodeId(channelTable.getValue(i, "DOWNNODE"));
                channels.addChannel(channel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        InputTable xsectionTable = getTableNamed("XSECT_LAYER");
        if (xsectionTable != null) {
            int nxsects = xsectionTable.getValues().size();
            boolean newLayer = true;
            String currentChannelDist = "";
            XSection currentXSection = null;
            for (int i = 0; i < nxsects; i++) {
                try {
                    if (!newLayer) {
                        String channelDist = xsectionTable.getValue(i, "CHAN_NO") + "_" + xsectionTable.getValue(i, "DIST");
                        if (!channelDist.equals(currentChannelDist)) {
                            newLayer = true;
                        }
                    }
                    if (newLayer) {
                        Channel channel = channels.getChannel(xsectionTable.getValue(i, "CHAN_NO"));
                        XSection xsection = new XSection();
                        xsection.setChannelId(channel.getId());
                        xsection.setDistance(Double.parseDouble(xsectionTable.getValue(i, "DIST")));
                        currentChannelDist = xsectionTable.getValue(i, "CHAN_NO") + "_" + xsectionTable.getValue(i, "DIST");
                        currentXSection = xsection;
                        channel.addXSection(xsection);
                        newLayer = false;
                    }
                    XSectionLayer layer = new XSectionLayer();
                    layer.setElevation(Double.parseDouble(xsectionTable.getValue(i, "ELEV")));
                    layer.setArea(Double.parseDouble(xsectionTable.getValue(i, "AREA")));
                    layer.setTopWidth(Double.parseDouble(xsectionTable.getValue(i, "WIDTH")));
                    layer.setWettedPerimeter(Double.parseDouble(xsectionTable.getValue(i, "WET_PERIM")));
                    currentXSection.addLayer(layer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        InputTable gisTable = getTableNamed("CHANNEL_GIS");
        if (gisTable != null) {
            try {
                int nvals = gisTable.getValues().size();
                for (int i = 0; i < nvals; i++) {
                    String id = gisTable.getValue(i, "ID");
                    Channel channel = channels.getChannel(id);
                    if (channel != null) {
                        channel.setLatLngPoints(TableUtil.toLatLngPoints(gisTable.getValue(i, "INTERIOR_LAT_LNG")));
                    }
                }
            } catch (Exception e) {
            }
        }
        InputTable xsectGisTable = getTableNamed("XSECTION_GIS");
        if (xsectGisTable != null) {
            try {
                int nvals = xsectGisTable.getValues().size();
                for (int i = 0; i < nvals; i++) {
                    String id = xsectGisTable.getValue(i, "ID");
                    String channelId = xsectGisTable.getValue(i, "CHAN_NO");
                    Channel channel = channels.getChannel(channelId);
                    if (channel != null) {
                        XSectionProfile xsProfile = new XSectionProfile();
                        xsProfile.setId(Integer.parseInt(id));
                        xsProfile.setChannelId(Integer.parseInt(channelId));
                        double dist = Double.parseDouble(xsectGisTable.getValue(i, "DIST"));
                        XSection xSectionAt = channel.getXSectionAt(dist);
                        if (xSectionAt == null) {
                            System.err.println("No XSection in channel: " + channel.getId() + " at distance: " + dist);
                            continue;
                        }
                        xSectionAt.setProfile(xsProfile);
                        xsProfile.setDistance(dist);
                        xsProfile.setEndPoints(TableUtil.toLatLngPoints(xsectGisTable.getValue(i, "LATLNG_ENDPOINTS")));
                        xsProfile.setProfilePoints(TableUtil.toProfilePoints(xsectGisTable.getValue(i, "PROFILE_POINTS")));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return channels;
    }
}
