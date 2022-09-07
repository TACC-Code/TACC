class BackupThread extends Thread {
    public int[] insertFlight(Flight _flight[], int currentPilot) throws SQLException {
        int[] result = new int[_flight.length];
        try {
            for (int x = 0; x < _flight.length; x++) {
                psta_insert.setInt(1, currentPilot);
                psta_insert.setInt(2, _flight[x].nr);
                psta_insert.setLong(3, _flight[x].dato.getTime());
                psta_insert.setInt(4, _flight[x].flytype_id);
                psta_insert.setString(5, _flight[x].startart);
                psta_insert.setInt(6, _flight[x].slaebetid);
                psta_insert.setInt(7, _flight[x].motortid);
                psta_insert.setInt(8, _flight[x].svaevetid);
                psta_insert.setInt(9, _flight[x].distance);
                psta_insert.setInt(10, _flight[x].startsted_id);
                psta_insert.setInt(11, _flight[x].landingssted_id);
                psta_insert.setString(12, _flight[x].note);
                psta_insert.setString(13, convertFromBoolean(_flight[x].straek));
                psta_insert.setString(14, convertFromBoolean(_flight[x].udelanding));
                psta_insert.setString(15, convertFromBoolean(_flight[x].kaptajn));
                psta_insert.setString(16, convertFromBoolean(_flight[x].instruktoer));
                psta_insert.setString(17, convertFromBoolean(_flight[x].forsaede));
                psta_insert.setString(18, convertFromBoolean(_flight[x].passager));
                psta_insert.setString(19, convertFromBoolean(_flight[x].afbrudtstart));
                psta_insert.executeUpdate();
                conn.commit();
                result[x] = getCurrvalFlyvning();
            }
        } catch (SQLException sqle) {
            conn.rollback();
            log.debug(sqle);
            throw sqle;
        }
        return result;
    }
}
