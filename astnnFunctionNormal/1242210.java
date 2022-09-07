class BackupThread extends Thread {
    public boolean getWaveform(List<StationIdentifierType> stations, String beginTime, String endTime, String filename) {
        boolean isError = false;
        DataRequestType dataRequest = new DataRequestType();
        dataRequest.setUserToken(userToken);
        TemporalBoundsType tempBound = new TemporalBoundsType();
        TimePeriodType timePeriod = new TimePeriodType();
        timePeriod.setFrame("#ISO-8601");
        TimePositionType beginPosition = new TimePositionType();
        beginPosition.setFrame("#ISO-8601");
        beginPosition.getValue().add(beginTime);
        TimePositionType endPosition = new TimePositionType();
        endPosition.setFrame("#ISO-8601");
        endPosition.getValue().add(endTime);
        timePeriod.setBeginPosition(beginPosition);
        timePeriod.setEndPosition(endPosition);
        tempBound.setTimePeriod(timePeriod);
        for (StationIdentifierType cur : stations) {
            cur.setTimeSpan(tempBound);
            dataRequest.getStationIdentifierFilter().add(cur);
            dataRequest.setDataFormat("MSEED");
        }
        System.out.print("Sending the data request...");
        DataStatusResponseType slDataResponse = binding.dataRequest(dataRequest);
        System.out.println("done.");
        System.out.print("Checking the status of the data...");
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
            System.out.print("\tDownloading the data...");
            try {
                URL url = new URL(dataItem.value.get(0).getDownloadToken().getDownloadURL());
                URLConnection con = url.openConnection();
                BufferedInputStream in = new BufferedInputStream(con.getInputStream());
                FileOutputStream out = new FileOutputStream(filename);
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
                isError = true;
            }
            System.out.println("done.");
        } else {
            System.out.println("Status not ok: " + routedRequestHolder.value.get(0).getStatusDescription());
            isError = true;
        }
        System.out.println("done.");
        System.out.print("Purging the data...");
        List<PurgeStatusResponseType> purgeStatusResponseList = new ArrayList<PurgeStatusResponseType>();
        purgeStatusResponseList.add(new PurgeStatusResponseType());
        Holder<List<PurgeStatusResponseType>> purgeStatusResponseHolder = new Holder<List<PurgeStatusResponseType>>(purgeStatusResponseList);
        binding.purgeData(userToken, requestId, purgeStatusResponseHolder, errorHolder);
        System.out.println("done.");
        return isError;
    }
}
