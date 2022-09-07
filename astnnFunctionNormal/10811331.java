class BackupThread extends Thread {
    public void run() {
        getLogger(m_event).logMessage(0, "Executing query: " + m_event.getConnection().getRequest());
        try {
            Socket socket = (Socket) provideResource(m_event);
            String request = (String) m_event.getConnection().getRequest();
            socket.getOutputStream().write(getStringLength(request));
            socket.getOutputStream().write(request.getBytes());
            InputStream inputStream = socket.getInputStream();
            int responseLength = getResponseLength(inputStream);
            getLogger(m_event).logMessage(0, "Simple Server Adapter: Response length is: " + responseLength);
            byte[] responseBytes = new byte[responseLength];
            inputStream.read(responseBytes);
            ((ServerConnection) m_event.getConnection()).setServerResource(this, socket);
            m_event.getConnection().setResponse(new String(responseBytes));
            getLogger(m_event).logMessage(0, "Simple Server Adapter received: " + new String(responseBytes));
        } catch (ServerResourceException e) {
            getLogger(m_event).logError(3, "Simple Server Adapter failed to obtain socket.");
            m_event.getConnection().setError(AppError.ms_serverError);
        } catch (Exception e) {
            getLogger(m_event).logError(3, "Simple Server Adapter failed to write or read from socket. " + "Exception: " + e);
            m_event.getConnection().setError(AppError.ms_serverError);
        }
    }
}
