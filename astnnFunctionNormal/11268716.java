class BackupThread extends Thread {
    private void getChannelByID(Integer chID) throws SQLException {
        String tableName = null;
        String sql = "SELECT CHANNELNAME,FINGERPRINTTABLENAME,SPACECOUPLING," + "MIDDLEWAREADAPTERID,ISWSDLBACKED,ISTIMEDECOUPLED,SUPPORTSINBOUND," + "SUPPORTSINVOKE,MSGSCHEMAIN,WSDLCHANNELURL,WSDLCHANNELPORTTYPE," + "WSDLCHANNELOPERATIONNAME,CHANNELMSGSCHEMAOUT FROM " + tableName + " WHERE channelID = ?";
        PreparedStatement psth = null;
        psth.setInt(1, chID);
        ResultSet rs = psth.executeQuery();
        if (rs.next()) {
            this.channelName = rs.getString("channelName");
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
        }
        rs.close();
        psth.close();
    }
}
