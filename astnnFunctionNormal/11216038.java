class BackupThread extends Thread {
    public void add(String trackName, String[] trackField, RGB trackColor) {
        hasDataToDisplay = true;
        String channel = trackName;
        int trackFieldCount = trackField.length;
        int trackFieldIndex;
        for (trackFieldIndex = 0; trackFieldIndex < trackFieldCount; trackFieldIndex++) {
            if (trackField[trackFieldIndex].compareTo("") != 0) {
                channel += ".";
                channel += trackField[trackFieldIndex];
            }
        }
        String[] channelArrayTmp = new String[numberOfLines];
        if (channelArray != null) {
            for (int i = 0; i < channelArray.length; i++) channelArrayTmp[i] = channelArray[i];
        }
        channelArrayTmp[numberOfLines - 1] = channel;
        channelArray = channelArrayTmp;
        DiagnosticMonitorMain.getDefault().addMessageNameSpecification(handle, trackName);
        DiagnosticMonitorMain.getDefault().setCurrentFilterIndex(handle, 0);
        solution.getDataModel().lock();
        SocratesProperties socratesProperties = new SocratesProperties(dpSource);
        socratesProperties.setPropertyValue((Object) "flow", (Object) new Boolean(false));
        socratesProperties.setPropertyValue((Object) "platoField", (Object) channel);
        IPaneViewPart paneViewPart = (IPaneViewPart) viewPart;
        if (stid.compareTo(Constants.lineGraphStid) == 0) {
            LineProp prop = (LineProp) paneViewPart.getProperties();
            ArrayList channelList = (ArrayList) prop.getChannelProps().clone();
            ChannelProp cp = new ChannelProp();
            cp.setFieldName(channel);
            cp.setForegroundRGB(trackColor);
            channelList.add(cp);
            prop.setChannelProps(channelList);
            if (numberOfLines > 1 && !prop.getShowLegend()) {
                prop.setShowLegend(true);
            }
            numberOfLines++;
            prop.getYRange().setPropertyValue((Object) "auto", (Object) new Boolean(false));
        } else if (stid.compareTo(Constants.stateGraphStid) == 0) {
            StateProp prop = (StateProp) paneViewPart.getProperties();
            String categoryName = Constants.platoCatDesignation + channel;
            prop.createCategory(categoryName);
            ArrayList channelList = (ArrayList) prop.getChannelProps().clone();
            StateFieldProp cp = new StateFieldProp();
            cp.setFieldName(channel);
            cp.setForegroundRGB(trackColor);
            cp.setStateCategoryName(categoryName);
            channelList.add(cp);
            prop.setChannelProps(channelList);
            numberOfLines++;
            prop.getYRange().setPropertyValue((Object) "auto", (Object) new Boolean(false));
        } else if (stid.compareTo(Constants.discreteGraphStid) == 0) {
            StateProp prop = (StateProp) paneViewPart.getProperties();
            String categoryName = Constants.platoCatDesignation + channel;
            prop.createCategory(categoryName);
            ArrayList channelList = (ArrayList) prop.getChannelProps().clone();
            StateFieldProp cp = new StateFieldProp();
            cp.setFieldName(channel);
            cp.setForegroundRGB(trackColor);
            cp.setStateCategoryName(categoryName);
            channelList.add(cp);
            prop.setChannelProps(channelList);
            numberOfLines++;
            prop.getYRange().setPropertyValue((Object) "auto", (Object) new Boolean(false));
        } else if (stid.compareTo(Constants.tableGraphStid) == 0) {
            TableProp prop = (TableProp) paneViewPart.getProperties();
            prop.addChannel();
            numberOfLines++;
        } else if (stid.compareTo(Constants.barGraphStid) == 0) {
            BarProp prop = (BarProp) paneViewPart.getProperties();
            String numberOfLinesStr = String.valueOf(numberOfLines);
            prop.setPropertyValue((Object) "addBar", (Object) new String(numberOfLinesStr));
            ArrayList channelList = (ArrayList) prop.getValueProps();
            ChannelProp cp = (ChannelProp) channelList.get(channelList.size() - 1);
            cp.setForegroundRGB(trackColor);
            cp.setFieldName(channel);
            prop.setValueProps(channelList);
            numberOfLines++;
            prop.getYRange().setPropertyValue((Object) "auto", (Object) new Boolean(false));
        }
        paneViewPart.updatePropertiesView();
        ((PlatoBuffer) dpBuffer).addChannel(trackName, trackField);
        ((PlatoBuffer) dpBuffer).calculateTimeShiftCount();
        solution.getDataModel().configureProcessor(null);
        solution.getDataModel().unLock();
        setChannelNamesExtension();
        socratesProperties.setPropertyValue((Object) "flow", (Object) new Boolean(true));
        for (int idx = 0; idx < channelArray.length; idx++) {
            String displayName = getDisplayNamePlato(idx);
            if (displayName != null && !displayName.equals("")) setDisplayNameSocrates(idx, getDisplayNamePlato(idx)); else {
                if (stid.compareTo(Constants.lineGraphStid) != 0) {
                    String[] proposedNameArray = channelArray[idx].split("\\.");
                    String proposedName = proposedNameArray[proposedNameArray.length - 1];
                    setDisplayNameSocrates(idx, proposedName);
                    setDisplayNamePlato(idx, proposedName);
                } else {
                    setDisplayNameSocrates(idx, channelArray[idx]);
                }
            }
        }
        for (int idx = 0; idx < channelArray.length; idx++) {
            int width = getFieldWidthPlato(idx + Constants.referenceFields.length);
            if (width == -1) width = 0;
            setFieldWidthSocrates(idx + Constants.referenceFields.length, width);
        }
    }
}
