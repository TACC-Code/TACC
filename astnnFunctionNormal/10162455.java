class BackupThread extends Thread {
    public List<InputTable> fromChannels(Channels channels) {
        ArrayList<InputTable> list = new ArrayList<InputTable>();
        InputTable channelTable = new InputTable();
        channelTable.setName("CHANNEL");
        channelTable.setHeaders(Arrays.asList(new String[] { "CHAN_NO", "LENGTH", "MANNING", "DISPERSION", "UPNODE", "DOWNNODE" }));
        ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> xvalues = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> xpvalues = new ArrayList<ArrayList<String>>();
        for (Channel channel : channels.getChannels()) {
            ArrayList<String> rowValues = new ArrayList<String>();
            rowValues.add(channel.getId());
            rowValues.add(channel.getLength() + "");
            rowValues.add(channel.getMannings() + "");
            rowValues.add(channel.getDispersion() + "");
            rowValues.add(channel.getUpNodeId());
            rowValues.add(channel.getDownNodeId());
            values.add(rowValues);
            for (XSection xsection : channel.getXsections()) {
                for (XSectionLayer xsectLayer : xsection.getLayers()) {
                    ArrayList<String> xrowValues = new ArrayList<String>();
                    xrowValues.add(channel.getId());
                    xrowValues.add(xsection.getDistance() + "");
                    xrowValues.add(xsectLayer.getElevation() + "");
                    xrowValues.add(xsectLayer.getArea() + "");
                    xrowValues.add(xsectLayer.getTopWidth() + "");
                    xrowValues.add(xsectLayer.getWettedPerimeter() + "");
                    xvalues.add(xrowValues);
                }
                if (xsection.getProfile() != null) {
                    XSectionProfile profile = xsection.getProfile();
                    ArrayList<String> pvalues = new ArrayList<String>();
                    pvalues.add(profile.getId() + "");
                    pvalues.add(channel.getId() + "");
                    pvalues.add(profile.getDistance() + "");
                    pvalues.add(TableUtil.fromLatLngPoints(profile.getEndPoints()));
                    pvalues.add(TableUtil.fromProfilePoints(profile.getProfilePoints()));
                    xpvalues.add(pvalues);
                }
            }
        }
        channelTable.setValues(values);
        list.add(channelTable);
        InputTable xsectionTable = new InputTable();
        xsectionTable.setName("XSECT_LAYER");
        xsectionTable.setHeaders(Arrays.asList(new String[] { "CHAN_NO", "DIST", "ELEV", "AREA", "WIDTH", "WET_PERIM" }));
        list.add(xsectionTable);
        xsectionTable.setValues(xvalues);
        InputTable gisTable = new InputTable();
        gisTable.setName("CHANNEL_GIS");
        gisTable.setHeaders(Arrays.asList(new String[] { "ID", "INTERIOR_LAT_LNG" }));
        values = new ArrayList<ArrayList<String>>();
        for (Channel channel : channels.getChannels()) {
            ArrayList<String> rowValues = new ArrayList<String>();
            rowValues.add(channel.getId());
            rowValues.add(TableUtil.fromLatLngPoints(channel.getLatLngPoints()));
            values.add(rowValues);
        }
        gisTable.setValues(values);
        list.add(gisTable);
        InputTable xsectionProfileTable = new InputTable();
        xsectionProfileTable.setName("XSECTION_GIS");
        xsectionProfileTable.setHeaders(Arrays.asList(new String[] { "ID", "CHAN_NO", "DIST", "LATLNG_ENDPOINTS", "PROFILE_POINTS" }));
        xsectionProfileTable.setValues(xpvalues);
        list.add(xsectionProfileTable);
        return list;
    }
}
