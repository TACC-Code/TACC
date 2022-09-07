class BackupThread extends Thread {
    private StockQuote getStockQuote(String symbol, HttpClient httpClient) throws InvalidBackendResponse {
        try {
            HttpUriRequest httpUriRequest = new HttpGet("http://www.webservicex.net/stockquote.asmx/GetQuote?symbol=" + symbol);
            HttpResponse httpResponse = httpClient.execute(httpUriRequest);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(httpResponse.getEntity().getContent());
            int contentLength = (int) httpResponse.getEntity().getContentLength();
            if (contentLength <= 0) {
                InvalidBackendResponse invalidBackendResponse = new InvalidBackendResponse(httpResponse.getStatusLine().getStatusCode(), httpResponse.getStatusLine().getReasonPhrase());
                System.out.println("Error while receiving response from backend, " + invalidBackendResponse);
                throw invalidBackendResponse;
            }
            byte[] bytes = new byte[contentLength];
            bufferedInputStream.read(bytes);
            bufferedInputStream.close();
            String responseText = new String(bytes, "utf-8");
            responseText = responseText.replaceAll("&lt;", "<");
            responseText = responseText.replaceAll("&gt;", ">");
            InputStream inputStream = new ByteArrayInputStream(responseText.getBytes());
            return parseStockQuoteFromWebServiceResponse(inputStream);
        } catch (InvalidBackendResponse e) {
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new InvalidBackendResponse(-1, e.getMessage());
        }
    }
}
