class BackupThread extends Thread {
    private void callServlet() {
        BufferedReader in = null;
        String inputLine;
        StringBuffer outputXML = new StringBuffer();
        boolean success = false;
        try {
            loadProperties();
            if ((requestNumber == null || requestNumber.equals("")) && (analysisNumber == null || analysisNumber.equals(""))) {
                this.printUsage();
                throw new Exception("Please specify all mandatory arguments.  See command line usage.");
            }
            if (server == null || server.equals("")) {
                this.printUsage();
                throw new Exception("Please specify all mandatory arguments.  See command line usage.");
            }
            trustCerts();
            URL url = new URL((server.equals("localhost") ? "http://" : "https://") + server + "/gnomex/login_verify.jsp?j_username=" + userName + "&j_password=" + password);
            URLConnection conn = url.openConnection();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            success = false;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                if (inputLine.indexOf("<SUCCESS") >= 0) {
                    success = true;
                    break;
                }
            }
            if (!success) {
                System.err.print(outputXML.toString());
                throw new Exception("Unable to login");
            }
            List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
            url = new URL((server.equals("localhost") ? "http://" : "https://") + server + "/gnomex/CreateSecurityAdvisor.gx");
            conn = url.openConnection();
            for (String cookie : cookies) {
                conn.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
            }
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            success = false;
            outputXML = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                outputXML.append(inputLine);
                if (inputLine.indexOf("<SecurityAdvisor") >= 0) {
                    success = true;
                    break;
                }
            }
            if (!success) {
                System.err.print(outputXML.toString());
                throw new Exception("Unable to create security advisor");
            }
            in.close();
            String parms = "";
            if (requestNumber != null) {
                parms = URLEncoder.encode("requestNumber", "UTF-8") + "=" + URLEncoder.encode(requestNumber, "UTF-8");
            } else if (analysisNumber != null) {
                parms = URLEncoder.encode("analysisNumber", "UTF-8") + "=" + URLEncoder.encode(analysisNumber, "UTF-8");
            }
            success = false;
            outputXML = new StringBuffer();
            url = new URL((server.equals("localhost") ? "http://" : "https://") + server + "/gnomex/FastDataTransferUploadStart.gx");
            conn = url.openConnection();
            conn.setDoOutput(true);
            for (String cookie : cookies) {
                conn.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
            }
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(parms);
            wr.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                System.out.print(inputLine);
                if (inputLine.indexOf("<FDTUploadUuid") >= 0) {
                    success = true;
                }
            }
            System.out.println();
            if (!success) {
                throw new Exception("Unable to create upload staging directory");
            }
        } catch (MalformedURLException e) {
            printUsage();
            e.printStackTrace();
            System.err.println(e.toString());
        } catch (IOException e) {
            printUsage();
            e.printStackTrace();
            System.err.println(e.toString());
        } catch (Exception e) {
            printUsage();
            e.printStackTrace();
            System.err.println(e.toString());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
