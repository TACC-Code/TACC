class BackupThread extends Thread {
    @Override
    public boolean sendMessage(ChatMessage message) {
        logger.debug("storing message in database");
        boolean result = false;
        Connection sqlConnection = ConnectionProvider.getInstance().getConnection();
        CallableStatement statement = null;
        try {
            statement = sqlConnection.prepareCall(SEND_MESSAGE_QUERY);
            statement.setString(1, message.getChannel());
            statement.setString(2, message.getMessage());
            statement.setString(3, message.getAuteur());
            statement.registerOutParameter(4, Types.BOOLEAN);
            statement.execute();
            result = statement.getBoolean(4);
        } catch (SQLException pException) {
            logger.error("erreur sql :" + pException.getMessage());
        } catch (Exception e) {
            logger.error("erreur generic :" + e.getMessage());
        } finally {
            DaoUtils.killConnection(sqlConnection, statement);
        }
        logger.debug("result : " + result);
        return result;
    }
}
