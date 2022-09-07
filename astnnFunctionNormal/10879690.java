class BackupThread extends Thread {
    private int fetchResultSet(ResultSet oRSet, int iSkip) throws SQLException, ArrayIndexOutOfBoundsException {
        Vector oRow;
        int iCol;
        int iRetVal = 0;
        int iMaxRow = iMaxRows < 0 ? 2147483647 : iMaxRows;
        long lFetchTime = 0;
        if (DebugFile.trace) {
            DebugFile.writeln("Begin DBSubset.fetchResultSet([ResultSet], " + String.valueOf(iSkip) + ")");
            DebugFile.incIdent();
            DebugFile.writeln("column count = " + String.valueOf(iColCount));
            DebugFile.writeln("max. rows = " + String.valueOf(iMaxRows));
            DebugFile.writeln("new Vector(" + String.valueOf(iFetch) + "," + String.valueOf(iFetch) + ")");
            lFetchTime = System.currentTimeMillis();
        }
        oResults = new Vector(iFetch, iFetch);
        if (0 != iSkip) {
            oRSet.next();
            if (DebugFile.trace) DebugFile.writeln("ResultSet.relative(" + String.valueOf(iSkip - 1) + ")");
            oRSet.relative(iSkip - 1);
        }
        boolean bHasNext = oRSet.next();
        while (bHasNext && iRetVal < iMaxRow) {
            iRetVal++;
            oRow = new Vector(iColCount);
            for (iCol = 1; iCol <= iColCount; iCol++) oRow.add(oRSet.getObject(iCol));
            oResults.add(oRow);
            bHasNext = oRSet.next();
        }
        if (0 == iRetVal || iRetVal < iMaxRow) {
            bEOF = true;
            if (DebugFile.trace) DebugFile.writeln("readed " + String.valueOf(iRetVal) + " rows eof() = true");
        } else {
            bEOF = !bHasNext;
            if (DebugFile.trace) DebugFile.writeln("readed max " + String.valueOf(iMaxRow) + " rows eof() = " + String.valueOf(bEOF));
        }
        if (DebugFile.trace) {
            DebugFile.writeln("fetching done in " + String.valueOf(System.currentTimeMillis() - lFetchTime) + " ms");
            DebugFile.decIdent();
            DebugFile.writeln("End DBSubset.fetchResultSet() : " + String.valueOf(iRetVal));
        }
        return iRetVal;
    }
}
