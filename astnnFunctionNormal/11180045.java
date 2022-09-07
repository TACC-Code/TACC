class BackupThread extends Thread {
        public static GPHUserDTO fromTransport(HttpRequestFactory requestFactory, GoogleUrl url) throws IOException {
            HttpRequest request = requestFactory.buildGetRequest(url);
            HttpResponse resp = request.execute();
            JsonHttpParser parser = new JsonHttpParser();
            parser.jsonFactory = new JacksonFactory();
            return parser.parse(resp, GPHUserDTO.class);
        }
}
