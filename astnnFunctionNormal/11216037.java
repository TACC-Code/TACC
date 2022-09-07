class BackupThread extends Thread {
    public void remove(int removeIndex) {
        if (channelArray == null) return;
        if (removeIndex >= channelArray.length) return;
        if (channelArray.length == 1) hasDataToDisplay = false;
        String categoryName = Constants.platoCatDesignation + channelArray[removeIndex];
        int channelArrayCount = channelArray.length;
        int channelArrayIndex = 0;
        String[] channelArrayResult = new String[channelArrayCount - 1];
        int guiderIndex = 0;
        for (channelArrayIndex = 0; channelArrayIndex < channelArrayCount - 1; channelArrayIndex++) {
            if (guiderIndex == removeIndex) guiderIndex++;
            channelArrayResult[channelArrayIndex] = channelArray[guiderIndex];
            guiderIndex++;
        }
        channelArray = channelArrayResult;
        channelArrayCount--;
        PlatoBuffer platoBuffer = ((PlatoBuffer) dpBuffer);
        int trackNameInstanceCount = 0;
        String trackName = platoBuffer.getChannelName(removeIndex);
        for (int i = 0; i < platoBuffer.getChannelCount(); i++) if (platoBuffer.getChannelName(i).equals(trackName)) trackNameInstanceCount++;
        if (trackNameInstanceCount == 1) {
            DiagnosticMonitorMain.getDefault().removeMessageNameSpecification(handle, trackName);
        }
        DiagnosticMonitorMain.getDefault().setCurrentFilterIndex(handle, 0);
        solution.getDataModel().lock();
        SocratesProperties socratesProperties = new SocratesProperties(dpSource);
        socratesProperties.setPropertyValue((Object) "flow", (Object) new Boolean(false));
        socratesProperties.setPropertyValue((Object) "platoField", (Object) null);
        String Nr1 = Constants.referenceFields[Constants.referenceFieldNr];
        String Nr2 = Constants.referenceFields[Constants.referenceFieldNrCompressed];
        String Nr3 = Constants.referenceFields[Constants.referenceFieldTime];
        socratesProperties.setPropertyValue((Object) "platoField", (Object) Nr1);
        socratesProperties.setPropertyValue((Object) "platoField", (Object) Nr2);
        socratesProperties.setPropertyValue((Object) "platoField", (Object) Nr3);
        for (channelArrayIndex = 0; channelArrayIndex < channelArrayCount; channelArrayIndex++) {
            socratesProperties.setPropertyValue((Object) "platoField", (Object) channelArray[channelArrayIndex]);
        }
        IPaneViewPart paneViewPart = (IPaneViewPart) viewPart;
        if (stid.compareTo(Constants.lineGraphStid) == 0) {
            LineProp prop = (LineProp) paneViewPart.getProperties();
            ArrayList channelList = (ArrayList) prop.getChannelProps().clone();
            channelList.remove(removeIndex);
            prop.setChannelProps(channelList);
            if (channelArrayCount == 1 && prop.getShowLegend()) {
                prop.setShowLegend(false);
            }
            numberOfLines--;
            prop.getYRange().setPropertyValue((Object) "auto", (Object) new Boolean(false));
        } else if (stid.compareTo(Constants.stateGraphStid) == 0) {
            StateProp prop = (StateProp) paneViewPart.getProperties();
            prop.removeCategory(categoryName);
            ArrayList channelList = (ArrayList) prop.getChannelProps().clone();
            channelList.remove(removeIndex);
            prop.setChannelProps(channelList);
            numberOfLines--;
            prop.getYRange().setPropertyValue((Object) "auto", (Object) new Boolean(false));
        } else if (stid.compareTo(Constants.discreteGraphStid) == 0) {
            StateProp prop = (StateProp) paneViewPart.getProperties();
            prop.removeCategory(categoryName);
            ArrayList channelList = (ArrayList) prop.getChannelProps().clone();
            channelList.remove(removeIndex);
            prop.setChannelProps(channelList);
            numberOfLines--;
            prop.getYRange().setPropertyValue((Object) "auto", (Object) new Boolean(false));
        } else if (stid.compareTo(Constants.tableGraphStid) == 0) {
            TableProp prop = (TableProp) paneViewPart.getProperties();
            prop.removeChannel(removeIndex);
            numberOfLines--;
        } else if (stid.compareTo(Constants.barGraphStid) == 0) {
            BarProp prop = (BarProp) paneViewPart.getProperties();
            prop.removeChannel(removeIndex);
            numberOfLines--;
            prop.getYRange().setPropertyValue((Object) "auto", (Object) new Boolean(false));
        }
        paneViewPart.updatePropertiesView();
        ((PlatoBuffer) dpBuffer).removeChannel(removeIndex);
        ((PlatoBuffer) dpBuffer).calculateTimeShiftCount();
        solution.getDataModel().configureProcessor(null);
        solution.getDataModel().unLock();
        setChannelNamesExtension();
        socratesProperties.setPropertyValue((Object) "flow", (Object) new Boolean(true));
        WorkspaceSaveContainer wsc = SocratesPlugin.getDefault().getWorkspaceSaveContainer();
        wsc.sourceManagerEraseChannel(this, removeIndex);
        if (channelArray.length == 0) updateLegendLook();
        for (int idx = 0; idx < channelArray.length; idx++) {
            String displayName = getDisplayNamePlato(idx);
            if (displayName != null && !displayName.equals("")) setDisplayNameSocrates(idx, getDisplayNamePlato(idx)); else {
                if (stid.compareTo(Constants.lineGraphStid) != 0 && stid.compareTo(Constants.tableGraphStid) != 0) {
                    String[] proposedNameArray = channelArray[idx].split("\\.");
                    String proposedName = proposedNameArray[proposedNameArray.length - 1];
                    setDisplayNameSocrates(idx, proposedName);
                    setDisplayNamePlato(idx, proposedName);
                } else {
                    setDisplayNameSocrates(idx, channelArray[idx]);
                }
            }
        }
        int referenceFieldsCount = Constants.referenceFields.length;
        int referenceFieldsIndex;
        for (referenceFieldsIndex = 0; referenceFieldsIndex < referenceFieldsCount; referenceFieldsIndex++) {
            int width = getFieldWidthPlato(referenceFieldsIndex);
            setFieldWidthSocrates(referenceFieldsIndex, width);
        }
        for (int idx = 0; idx < channelArray.length; idx++) {
            int width = getFieldWidthPlato(idx + Constants.referenceFields.length);
            setFieldWidthSocrates(idx + Constants.referenceFields.length, width);
        }
    }
}
