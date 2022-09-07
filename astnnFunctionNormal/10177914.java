class BackupThread extends Thread {
        public void run() {
            try {
                Thread.sleep(600000);
            } catch (InterruptedException e) {
                Logger.debug(this, e.getMessage(), e);
            }
            String address = null;
            String hostname = "unknown";
            try {
                InetAddress addr = InetAddress.getLocalHost();
                byte[] ipAddr = addr.getAddress();
                addr = InetAddress.getByAddress(ipAddr);
                address = addr.getHostAddress();
                hostname = addr.getHostName();
            } catch (Exception e) {
                Logger.debug(this, "InitThread broke:", e);
            }
            try {
                String defaultHost = HostFactory.getDefaultHost().getHostname();
                StringBuilder sb = new StringBuilder();
                List<Host> hosts = HostFactory.getAllHosts();
                for (Host h : hosts) {
                    sb.append(h.getHostname() + "\n");
                    if (UtilMethods.isSet(h.getAliases())) {
                        sb.append(h.getAliases());
                    }
                }
                StringBuilder data = new StringBuilder();
                data.append(URLEncoder.encode("ipAddr", "UTF-8"));
                data.append("=");
                data.append(URLEncoder.encode(address, "UTF-8"));
                data.append("&");
                data.append(URLEncoder.encode("hostname", "UTF-8"));
                data.append("=");
                data.append(URLEncoder.encode(hostname, "UTF-8"));
                data.append("&");
                data.append(URLEncoder.encode("defaultHost", "UTF-8"));
                data.append("=");
                data.append(URLEncoder.encode(defaultHost, "UTF-8"));
                data.append("&");
                data.append(URLEncoder.encode("allHosts", "UTF-8"));
                data.append("=");
                data.append(URLEncoder.encode(sb.toString(), "UTF-8"));
                data.append("&");
                data.append(URLEncoder.encode("version", "UTF-8"));
                data.append("=");
                data.append(URLEncoder.encode(ReleaseInfo.getReleaseInfo(), "UTF-8"));
                data.append("&");
                data.append(URLEncoder.encode("build", "UTF-8"));
                data.append("=");
                data.append(URLEncoder.encode(String.valueOf(ReleaseInfo.getBuildNumber()), "UTF-8"));
                sb.delete(0, sb.length());
                sb.append("h");
                sb.append("tt");
                sb.append("p");
                sb.append(":");
                sb.append("//");
                sb.append("p");
                sb.append("i");
                sb.append("n");
                sb.append("g");
                sb.append(".");
                sb.append("d");
                sb.append("ot");
                sb.append("cms");
                sb.append(".");
                sb.append("or");
                sb.append("g/");
                sb.append("servlets/TB");
                sb.append("Information");
                URL url = new URL(sb.toString());
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setUseCaches(false);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(data.toString());
                wr.flush();
                wr.close();
                DataInputStream input = new DataInputStream(conn.getInputStream());
                input.close();
            } catch (UnknownHostException e) {
                Logger.debug(this, "Unable to get Hostname", e);
            } catch (Exception e) {
                Logger.debug(this, "InitThread broke:", e);
            } finally {
                DotHibernate.closeSession();
            }
        }
}
