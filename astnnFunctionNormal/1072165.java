class BackupThread extends Thread {
    public static void main(String[] args) {
        Seismolink_Service service = new Seismolink_Service();
        Seismolink binding = service.getSeismolinkSOAP12Binding();
        InventoryRequestType requestParam = new InventoryRequestType();
        UserTokenType userToken = new UserTokenType();
        userToken.setEmail("smertl@mail.tuwien.ac.at");
        requestParam.setUserToken(userToken);
        StationIdentifierType stationIdentifier = new StationIdentifierType();
        TemporalBoundsType tempBound = new TemporalBoundsType();
        TimePeriodType timePeriod = new TimePeriodType();
        timePeriod.setFrame("#ISO-8601");
        TimePositionType beginPosition = new TimePositionType();
        beginPosition.setFrame("#ISO-8601");
        beginPosition.getValue().add("2009-04-06T00:00:00");
        TimePositionType endPosition = new TimePositionType();
        endPosition.setFrame("#ISO-8601");
        endPosition.getValue().add("2009-04-06T06:00:00");
        timePeriod.setBeginPosition(beginPosition);
        timePeriod.setEndPosition(endPosition);
        tempBound.setTimePeriod(timePeriod);
        stationIdentifier.setTimeSpan(tempBound);
        stationIdentifier.setNetworkCode("OE");
        stationIdentifier.setStationCode("CONA");
        requestParam.getStationIdentifierFilter().add(stationIdentifier);
        System.out.println("Sending the request...");
        InventoryResponseType slResponse = binding.getInventory(requestParam);
        System.out.println("done.");
        InventoryType slArclinkInventory = slResponse.getArclinkInventory();
        Inventory inventory = (Inventory) slArclinkInventory.getAny();
        List<Object> networkList = (List<Object>) inventory.getAux_DeviceOrSeismometerOrResp_Paz();
        for (Object next : networkList) {
            Network curNet = (Network) next;
            System.out.println("++++++++++ NETWORK ++++++++++++++++");
            System.out.println("code: " + curNet.getCode());
            System.out.println();
            System.out.println("Stations:");
            List<Station> stationList = curNet.getStation();
            for (Station curStation : stationList) {
                System.out.println("code: " + curStation.getCode());
                System.out.println("country: " + curStation.getCountry());
                System.out.println();
            }
        }
        DataRequestType dataRequest = new DataRequestType();
        dataRequest.setUserToken(userToken);
        StationIdentifierType stationIdentifier1 = new StationIdentifierType();
        stationIdentifier1.setNetworkCode("OE");
        stationIdentifier1.setStationCode("ARSA");
        stationIdentifier1.setChannelCode("BHZ");
        stationIdentifier1.setTimeSpan(tempBound);
        dataRequest.getStationIdentifierFilter().add(stationIdentifier1);
        StationIdentifierType stationIdentifier2 = new StationIdentifierType();
        stationIdentifier2.setNetworkCode("OE");
        stationIdentifier2.setStationCode("MOA");
        stationIdentifier2.setChannelCode("BHE");
        stationIdentifier2.setTimeSpan(tempBound);
        dataRequest.getStationIdentifierFilter().add(stationIdentifier2);
        dataRequest.setDataFormat("MSEED");
        System.out.println("Sending the data request...");
        DataStatusResponseType slDataResponse = binding.dataRequest(dataRequest);
        System.out.println("done.");
        System.out.println("Checking the status of the data...");
        List<String> requestId = new ArrayList<String>();
        requestId.add(slDataResponse.getRoutedRequest().get(0).getId());
        Holder<List<RoutedRequestType>> routedRequestHolder = new Holder<List<RoutedRequestType>>(slDataResponse.getRoutedRequest());
        Holder<ErrorType> errorHolder = new Holder<ErrorType>(slDataResponse.getError());
        while (!routedRequestHolder.value.get(0).getReadyFlag().equals("true")) {
            binding.checkStatus(userToken, requestId, routedRequestHolder, errorHolder);
        }
        System.out.println("done.");
        System.out.println("Retrieving the data...");
        if (routedRequestHolder.value.get(0).getStatusDescription().matches(".*Status: OK.*")) {
            List<DataItemCollectionType> dataItemCollectionList = new ArrayList<DataItemCollectionType>();
            dataItemCollectionList.add(new DataItemCollectionType());
            dataItemCollectionList.get(0).getDataItem().add(new DataItemType());
            Holder<List<DataItemCollectionType>> dataSet = new Holder<List<DataItemCollectionType>>();
            Holder<List<DataItemType>> dataItem = new Holder<List<DataItemType>>(dataItemCollectionList.get(0).getDataItem());
            binding.dataRetrieve(userToken, requestId, dataItem, dataSet, errorHolder);
            System.out.println("Downloading the data...");
            try {
                URL url = new URL(dataItem.value.get(0).getDownloadToken().getDownloadURL());
                URLConnection con = url.openConnection();
                BufferedInputStream in = new BufferedInputStream(con.getInputStream());
                FileOutputStream out = new FileOutputStream("/home/stefan/Desktop/seismolink/test.msd");
                int i = 0;
                byte[] bytesIn = new byte[1024];
                while ((i = in.read(bytesIn)) >= 0) {
                    out.write(bytesIn, 0, i);
                }
                out.close();
                in.close();
            } catch (Exception e) {
                System.out.println("Error connecting to ftp.");
                System.out.println(e.getMessage());
            }
            System.out.println("done.");
        } else {
            System.out.println("Status not ok: " + routedRequestHolder.value.get(0).getStatusDescription());
        }
        System.out.println("done.");
        System.out.println("Purging the data...");
        List<PurgeStatusResponseType> purgeStatusResponseList = new ArrayList<PurgeStatusResponseType>();
        purgeStatusResponseList.add(new PurgeStatusResponseType());
        Holder<List<PurgeStatusResponseType>> purgeStatusResponseHolder = new Holder<List<PurgeStatusResponseType>>(purgeStatusResponseList);
        binding.purgeData(userToken, requestId, purgeStatusResponseHolder, errorHolder);
        System.out.println("done.");
        System.out.println("Everything done!");
    }
}
