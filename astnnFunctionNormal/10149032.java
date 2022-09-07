class BackupThread extends Thread {
    public synchronized void processEventScalar(final ScalarEvent scalarEvent) throws ArchivingException {
        try {
            String readValue = scalarEvent.valueToString(0);
            String writeValue = scalarEvent.valueToString(1);
            final long timeStampValue = scalarEvent.getTimeStamp();
            if (isValidLine(timeStampValue)) {
                doExport();
                if (dbProxy.getDataBase().getDbConn().getDbType() == ConfigConst.TDB_ORACLE) {
                    if (readValue == null || GlobalConst.ARCHIVER_NULL_VALUE.equalsIgnoreCase(readValue.trim())) {
                        readValue = GlobalConst.ORACLE_NULL_VALUE;
                    }
                    if (writeValue == null || GlobalConst.ARCHIVER_NULL_VALUE.equalsIgnoreCase(writeValue.trim())) {
                        writeValue = GlobalConst.ORACLE_NULL_VALUE;
                    }
                    if (scalarEvent.getData_type() == TangoConst.Tango_DEV_STRING) {
                        readValue = StringFormater.formatStringToWrite(readValue);
                        writeValue = StringFormater.formatStringToWrite(writeValue);
                    }
                    final StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("\"");
                    stringBuffer.append(DateUtil.milliToString(timeStampValue, DateUtil.FR_DATE_PATTERN));
                    stringBuffer.append("\"");
                    stringBuffer.append(",");
                    switch(scalarEvent.getWritable()) {
                        case AttrWriteType._READ:
                            stringBuffer.append("\"").append(readValue).append("\"");
                            break;
                        case AttrWriteType._READ_WRITE:
                        case AttrWriteType._READ_WITH_WRITE:
                            stringBuffer.append("\"").append(readValue).append("\"");
                            stringBuffer.append(",");
                            stringBuffer.append("\"").append(writeValue).append("\"");
                            break;
                        case AttrWriteType._WRITE:
                            stringBuffer.append("\"").append(writeValue).append("\"");
                            break;
                    }
                    write(stringBuffer.toString());
                    write(ConfigConst.NEW_LINE);
                } else if (dbProxy.getDataBase().getDbConn().getDbType() == ConfigConst.TDB_MYSQL) {
                    if (readValue == null || GlobalConst.ARCHIVER_NULL_VALUE.equalsIgnoreCase(readValue.trim())) {
                        readValue = GlobalConst.MYSQL_NULL_VALUE;
                    }
                    if (writeValue == null || GlobalConst.ARCHIVER_NULL_VALUE.equalsIgnoreCase(writeValue.trim())) {
                        writeValue = GlobalConst.MYSQL_NULL_VALUE;
                    }
                    if (scalarEvent.getData_type() == TangoConst.Tango_DEV_STRING) {
                        readValue = StringFormater.formatStringToWrite(readValue);
                        writeValue = StringFormater.formatStringToWrite(writeValue);
                    }
                    switch(scalarEvent.getWritable()) {
                        case AttrWriteType._READ:
                            write(new StringBuffer().append(toDbTimeStringMySQL(scalarEvent.getTimeStamp())).append(ConfigConst.FIELDS_LIMIT).append(readValue).append(ConfigConst.LINES_LIMIT).toString());
                            break;
                        case AttrWriteType._READ_WITH_WRITE:
                            write(new StringBuffer().append(toDbTimeStringMySQL(scalarEvent.getTimeStamp())).append(ConfigConst.FIELDS_LIMIT).append(readValue).append(ConfigConst.FIELDS_LIMIT).append(writeValue).append(ConfigConst.LINES_LIMIT).toString());
                            break;
                        case AttrWriteType._WRITE:
                            write(new StringBuffer().append(toDbTimeStringMySQL(scalarEvent.getTimeStamp())).append(ConfigConst.FIELDS_LIMIT).append(writeValue).append(ConfigConst.LINES_LIMIT).toString());
                            break;
                        case AttrWriteType._READ_WRITE:
                            write(new StringBuffer().append(toDbTimeStringMySQL(scalarEvent.getTimeStamp())).append(ConfigConst.FIELDS_LIMIT).append(readValue).append(ConfigConst.FIELDS_LIMIT).append(writeValue).append(ConfigConst.LINES_LIMIT).toString());
                            break;
                    }
                }
            } else {
                logger.debug("This timestamps has already been inserted : " + new Timestamp(timeStampValue) + " in the file " + fileName + "for " + scalarEvent.getAttribute_complete_name());
            }
        } catch (final IOException e) {
            e.printStackTrace();
            logger.error("IOException for " + scalarEvent.getAttribute_complete_name());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unknow Exception for " + scalarEvent.getAttribute_complete_name());
        }
    }
}
