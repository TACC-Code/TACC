class BackupThread extends Thread {
    private void getChannelByName(String chName) throws SQLException {
        String tableName = null;
        String sql = "SELECT CHANNELID,FINGERPRINTTABLENAME,SPACECOUPLING," + "MIDDLEWAREADAPTERID,ISWSDLBACKED,ISTIMEDECOUPLED,SUPPORTSINBOUND," + "SUPPORTSINVOKE,MSGSCHEMAIN,WSDLCHANNELURL,WSDLCHANNELPORTTYPE," + "WSDLCHANNELOPERATIONNAME,CHANNELMSGSCHEMAOUT FROM " + tableName + " WHERE channelName = ?";
        PreparedStatement psth = null;
        psth.setString(1, chName);
        ResultSet rs = psth.executeQuery();
        if (rs.next()) {
            this.setChannelID(rs.getInt("channelID"));
            this.setFingerprintTableName(rs.getString("FINGERPRINTTABLENAME"));
            this.setSpaceCoupling(rs.getString("SPACECOUPLING"));
            this.setMiddlewareAdapterId(rs.getString("MIDDLEWAREADAPTERID"));
            this.setIsWSDLBacked(rs.getString("ISWSDLBACKED"));
            this.setIsTimeDecoupled(rs.getString("ISTIMEDECOUPLED"));
            this.setSupportsInbound(rs.getString("SUPPORTSINBOUND"));
            this.setSupportsInvoke(rs.getString("SUPPORTSINVOKE"));
            this.setMsgSchemaIn(rs.getString("MSGSCHEMAIN"));
            this.setWsdlChannelUrl(rs.getString("WSDLCHANNELURL"));
            this.setWsdlChannelPortType(rs.getString("WSDLCHANNELPORTTYPE"));
            this.setWsdlChannelOperationName(rs.getString("WSDLCHANNELOPERATIONNAME"));
            this.setChannelMsgSchemaOut(rs.getString("CHANNELMSGSCHEMAOUT"));
            this.channelName = chName;
        }
        rs.close();
        psth.close();
    }
}
