class BackupThread extends Thread {
    public static void setManagers() {
        System.out.println("START setManagers");
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            URL url = new URL("http://co01sgd/sgdcent/cnt/vw_ss_ma_empleados_activos_to_xml.asp");
            InputStream inputStream = url.openStream();
            Document doc = db.parse(inputStream);
            doc.getDocumentElement().normalize();
            NodeList employees = doc.getElementsByTagName("empleado");
            percentCounter = 0D;
            percentAnt = 0;
            percentGlobal = employees.getLength();
            for (int emp = 0; emp < employees.getLength(); emp++) {
                Node fstNode = employees.item(emp);
                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element employee = (Element) fstNode;
                    Integer codigoEmpleado = Integer.parseInt((employee.getElementsByTagName("codigo_empleado").item(0).getChildNodes().item(0)).getNodeValue().toString().trim());
                    Integer mgrEmpId = Integer.parseInt((employee.getElementsByTagName("mgr_emp_id").item(0).getChildNodes().item(0)).getNodeValue().toString().trim());
                    VwSsMaEmpleadosActivos result = updateEmployee(codigoEmpleado, mgrEmpId);
                }
                showPercent("setManagers");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("END setManagers");
    }
}
