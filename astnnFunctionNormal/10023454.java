class BackupThread extends Thread {
    public void setValues(Statistics readerStatistics, Statistics writerStatistics, boolean isSuccess) {
        byteCount = readerStatistics.get(DataReaderStatistics.READ_BYTE_COUNT);
        filterMillis = writerStatistics.get(DataWriterStatisticConstants.FILTERMILLIS);
        databaseMillis = writerStatistics.get(DataWriterStatisticConstants.DATABASEMILLIS);
        statementCount = writerStatistics.get(DataWriterStatisticConstants.STATEMENTCOUNT);
        fallbackInsertCount = writerStatistics.get(DataWriterStatisticConstants.FALLBACKINSERTCOUNT);
        fallbackUpdateCount = writerStatistics.get(DataWriterStatisticConstants.FALLBACKUPDATECOUNT);
        missingDeleteCount = writerStatistics.get(DataWriterStatisticConstants.MISSINGDELETECOUNT);
        ignoreCount = writerStatistics.get(DataWriterStatisticConstants.IGNORECOUNT);
        lastUpdatedTime = new Date();
        if (!isSuccess) {
            failedRowNumber = statementCount;
            failedLineNumber = writerStatistics.get(DataWriterStatisticConstants.LINENUMBER);
        }
    }
}
