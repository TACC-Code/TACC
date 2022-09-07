class BackupThread extends Thread {
    private void postData(String solrUrlString, Reader data, StringBuffer output) throws GenericSearchException {
        URL solrUrl = null;
        try {
            solrUrl = new URL(solrUrlString);
        } catch (MalformedURLException e) {
            throw new GenericSearchException("solrUrl=" + solrUrl.toString() + ": ", e);
        }
        HttpURLConnection urlc = null;
        String POST_ENCODING = "UTF-8";
        try {
            urlc = (HttpURLConnection) solrUrl.openConnection();
            try {
                urlc.setRequestMethod("POST");
            } catch (ProtocolException e) {
                throw new GenericSearchException("Shouldn't happen: HttpURLConnection doesn't support POST??", e);
            }
            urlc.setDoOutput(true);
            urlc.setDoInput(true);
            urlc.setUseCaches(false);
            urlc.setAllowUserInteraction(false);
            urlc.setRequestProperty("Content-type", "text/xml; charset=" + POST_ENCODING);
            OutputStream out = urlc.getOutputStream();
            try {
                Writer writer = new OutputStreamWriter(out, POST_ENCODING);
                pipe(data, writer);
                writer.close();
            } catch (IOException e) {
                throw new GenericSearchException("IOException while posting data", e);
            } finally {
                if (out != null) out.close();
            }
            InputStream in = urlc.getInputStream();
            int status = urlc.getResponseCode();
            StringBuffer errorStream = new StringBuffer();
            try {
                if (status != HttpURLConnection.HTTP_OK) {
                    errorStream.append("postData URL=" + solrUrlString + " HTTP response code=" + status + " ");
                    throw new GenericSearchException("URL=" + solrUrlString + " HTTP response code=" + status);
                }
                Reader reader = new InputStreamReader(in);
                pipeString(reader, output);
                reader.close();
            } catch (IOException e) {
                throw new GenericSearchException("IOException while reading response", e);
            } finally {
                if (in != null) in.close();
            }
            InputStream es = urlc.getErrorStream();
            if (es != null) {
                try {
                    Reader reader = new InputStreamReader(es);
                    pipeString(reader, errorStream);
                    reader.close();
                } catch (IOException e) {
                    throw new GenericSearchException("IOException while reading response", e);
                } finally {
                    if (es != null) es.close();
                }
            }
            if (errorStream.length() > 0) {
                throw new GenericSearchException("postData error: " + errorStream.toString());
            }
        } catch (IOException e) {
            throw new GenericSearchException("Connection error (is Solr running at " + solrUrl + " ?): " + e);
        } finally {
            if (urlc != null) urlc.disconnect();
        }
    }
}
