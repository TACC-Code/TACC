class BackupThread extends Thread {
    public void execute(PaymentInfoMagcard payinfo) {
        StringBuffer sb = new StringBuffer();
        try {
            sb.append("pg_merchant_id=");
            sb.append(m_sCommerceID);
            sb.append("&pg_password=");
            sb.append(m_sCommercePassword);
            sb.append("&pg_total_amount=");
            NumberFormat formatter = new DecimalFormat("0000.00");
            String amount = formatter.format(Math.abs(payinfo.getTotal()));
            sb.append(URLEncoder.encode(amount.replace(',', '.'), "UTF-8"));
            if (payinfo.getTrack1(true) != null) {
                sb.append("&pg_cc_swipe_data=");
                sb.append(URLEncoder.encode(payinfo.getTrack1(true), "UTF-8"));
            } else {
                sb.append("&ecom_payment_card_type=");
                sb.append(getCardType(payinfo.getCardNumber()));
                sb.append("&ecom_payment_card_number=");
                sb.append(URLEncoder.encode(payinfo.getCardNumber(), "UTF-8"));
                sb.append("&ecom_payment_card_expdate_month=");
                String tmp = payinfo.getExpirationDate();
                sb.append(tmp.substring(0, 2));
                sb.append("&ecom_payment_card_expdate_year=");
                sb.append(tmp.substring(2, tmp.length()));
                String[] cc_name = payinfo.getHolderName().split(" ");
                sb.append("&ecom_billto_postal_name_first=");
                if (cc_name.length > 0) {
                    sb.append(URLEncoder.encode(cc_name[0], "UTF-8"));
                }
                sb.append("&ecom_billto_postal_name_last=");
                if (cc_name.length > 1) {
                    sb.append(URLEncoder.encode(cc_name[1], "UTF-8"));
                }
                sb.append("&ecom_payment_card_name=");
                sb.append(payinfo.getHolderName());
            }
            if (payinfo.getTotal() >= 0.0) {
                sb.append("&pg_transaction_type=");
                sb.append(SALE);
            } else {
                sb.append("&pg_transaction_type=");
                sb.append(REFUND);
            }
            sb.append("&endofdata");
            URL url = new URL(ENDPOINTADDRESS);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(sb.toString().getBytes());
            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String returned = "", aux;
            while ((aux = in.readLine()) != null) {
                returned += "&" + aux;
            }
            payinfo.setReturnMessage(returned);
            in.close();
            Map props = new HashMap();
            StringTokenizer tk = new java.util.StringTokenizer(returned, "&");
            while (tk.hasMoreTokens()) {
                String sToken = tk.nextToken();
                int i = sToken.indexOf('=');
                if (i >= 0) {
                    props.put(URLDecoder.decode(sToken.substring(0, i), "UTF-8"), URLDecoder.decode(sToken.substring(i + 1), "UTF-8"));
                } else {
                    props.put(URLDecoder.decode(sToken, "UTF-8"), null);
                }
            }
            if (APPROVED.equals(props.get("pg_response_type"))) {
                payinfo.paymentOK((String) props.get("pg_authorization_code"), (String) props.get("pg_trace_number"), returned);
            } else {
                String sCode = (String) props.get("pg_response_description");
                sCode = sCode.replace("F01", "\nMANDITORY FIELD MISSING");
                sCode = sCode.replace("F03", "\nINVALID FIELD NAME");
                sCode = sCode.replace("F04", "\nINVALID FIELD VALUE");
                sCode = sCode.replace("F05", "\nDUPLICATE FIELD");
                sCode = sCode.replace("F07", "\nCONFLICTING FIELD");
                payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), sCode);
            }
        } catch (UnsupportedEncodingException eUE) {
            payinfo.paymentError(AppLocal.getIntString("message.paymentexceptionservice"), eUE.getMessage());
        } catch (MalformedURLException eMURL) {
            payinfo.paymentError(AppLocal.getIntString("message.paymentexceptionservice"), eMURL.getMessage());
        } catch (IOException e) {
            payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), e.getMessage());
        }
    }
}
