class BackupThread extends Thread {
    public static String renderPropertyDescriptorEditor(ServletRequest request, ServletResponse response, String formParameterName, PropertyDescriptor pd, Object bean) {
        if (pd.getReadMethod() != null) {
            Class returnType = pd.getReadMethod().getReturnType();
            try {
                StringWriter buf = new StringWriter();
                if (pd.getReadMethod().getParameterTypes().length == 0) {
                    Object obj = pd.getReadMethod().invoke(bean, null);
                    if (obj == null) {
                        if (pd.getWriteMethod() != null) {
                            if (request.getParameter(formParameterName + "_newInstance") != null) {
                                obj = pd.getReadMethod().getReturnType().newInstance();
                                pd.getWriteMethod().invoke(bean, new Object[] { obj });
                            }
                        }
                    }
                    if (boolean.class.equals(returnType) || Boolean.class.equals(returnType)) {
                        boolean val = ((Boolean) obj).booleanValue();
                        boolean in = "on".equalsIgnoreCase(request.getParameter(formParameterName));
                        if (in != val) {
                            pd.getWriteMethod().invoke(bean, new Object[] { new Boolean(in) });
                            val = ((Boolean) pd.getReadMethod().invoke(bean, null)).booleanValue();
                        }
                        if (pd.getWriteMethod() == null) {
                            buf.write("<input name='");
                            buf.write(formParameterName);
                            buf.write("' type='checkbox'");
                            if (val) buf.write(" checked");
                            buf.write("' enabled='false'>");
                        } else {
                            buf.write("<input name='");
                            buf.write(formParameterName);
                            buf.write("' type='checkbox'");
                            if (val) buf.write(" checked");
                            buf.write(">");
                        }
                    } else if (short.class.equals(returnType) || Short.class.equals(returnType)) {
                        short val = ((Short) obj).shortValue();
                        if (request.getParameterMap().containsKey(formParameterName)) {
                            short in = Short.parseShort(request.getParameter(formParameterName));
                            if (in != val) {
                                pd.getWriteMethod().invoke(bean, new Object[] { new Short(in) });
                                val = ((Short) pd.getReadMethod().invoke(bean, null)).shortValue();
                            }
                        }
                        if (pd.getWriteMethod() == null) {
                            buf.write(String.valueOf(val));
                        } else {
                            buf.write("<input name='");
                            buf.write(formParameterName);
                            buf.write("' type='text' size=16 value='");
                            buf.write(String.valueOf(val));
                            buf.write("'>");
                        }
                    } else if (int.class.equals(returnType) || Integer.class.equals(returnType)) {
                        int val = ((Integer) obj).intValue();
                        if (request.getParameterMap().containsKey(formParameterName)) {
                            int in = Integer.parseInt(request.getParameter(formParameterName));
                            if (in != val) {
                                pd.getWriteMethod().invoke(bean, new Object[] { new Integer(in) });
                                val = ((Integer) pd.getReadMethod().invoke(bean, null)).intValue();
                            }
                        }
                        if (pd.getWriteMethod() == null) {
                            buf.write(String.valueOf(val));
                        } else {
                            buf.write("<input name='");
                            buf.write(formParameterName);
                            buf.write("' type='text' size=32 value='");
                            buf.write(String.valueOf(val));
                            buf.write("'>");
                        }
                    } else if (long.class.equals(returnType) || Long.class.equals(returnType)) {
                        long val = ((Long) obj).longValue();
                        if (request.getParameterMap().containsKey(formParameterName)) {
                            long in = Long.parseLong(request.getParameter(formParameterName));
                            if (in != val) {
                                pd.getWriteMethod().invoke(bean, new Object[] { new Long(in) });
                                val = ((Long) pd.getReadMethod().invoke(bean, null)).longValue();
                            }
                        }
                        if (pd.getWriteMethod() == null) {
                            buf.write(String.valueOf(val));
                        } else {
                            buf.write("<input name='");
                            buf.write(formParameterName);
                            buf.write("' type='text' size=64 value='");
                            buf.write(String.valueOf(val));
                            buf.write("'>");
                        }
                    } else if (double.class.equals(returnType) || Double.class.equals(returnType)) {
                        double val = ((Double) obj).doubleValue();
                        if (request.getParameterMap().containsKey(formParameterName)) {
                            double in = Double.parseDouble(request.getParameter(formParameterName));
                            if (in != val) {
                                pd.getWriteMethod().invoke(bean, new Object[] { new Double(in) });
                                val = ((Double) pd.getReadMethod().invoke(bean, null)).doubleValue();
                            }
                        }
                        if (pd.getWriteMethod() == null) {
                            buf.write(String.valueOf(val));
                        } else {
                            buf.write("<input name='");
                            buf.write(formParameterName);
                            buf.write("' type='text' size=64 value='");
                            buf.write(String.valueOf(val));
                            buf.write("'>");
                        }
                    } else if (obj instanceof String) {
                        String val = (String) obj;
                        if (pd.getWriteMethod() != null) {
                            String in;
                            if ((in = request.getParameter(formParameterName)) != null && !in.equals(val)) {
                                pd.getWriteMethod().invoke(bean, new Object[] { in });
                                val = (String) pd.getReadMethod().invoke(bean, null);
                            }
                            buf.write("<input name='");
                            buf.write(formParameterName);
                            buf.write("' type='text' size='75' value='");
                            buf.write(val);
                            buf.write("'>");
                        } else {
                            buf.write(val);
                        }
                    } else if (obj instanceof Date) {
                        Date d = (Date) obj;
                        String formattedDate = sdf.format(d);
                        String in;
                        if ((in = request.getParameter(formParameterName)) != null && !in.equals(formattedDate)) {
                            pd.getWriteMethod().invoke(bean, new Object[] { sdf.parse(in) });
                            formattedDate = sdf.format((Date) pd.getReadMethod().invoke(bean, null));
                        }
                        if (pd.getWriteMethod() == null) {
                            buf.write(formattedDate);
                        } else {
                            buf.write("<input name='");
                            buf.write(formParameterName);
                            buf.write("' type='text' size=20 value='");
                            buf.write(formattedDate);
                            buf.write("'>");
                        }
                    } else if (obj instanceof Collection) {
                        Collection coll = (Collection) obj;
                        if (request.getParameter(formParameterName + "_newInstance") != null && !"".equals(request.getParameter(formParameterName + "_newInstance"))) {
                            Object instance = Class.forName(request.getParameter(formParameterName + "_newInstance"));
                            coll.add(instance);
                            buf.write("New instance added to list<br>.");
                        }
                        buf.write("<br>");
                        buf.write("<input type='text' size=35 name='");
                        buf.write(formParameterName);
                        buf.write("_newInstance");
                        buf.write("'><input type='submit' value='add new instance to list'><br>");
                        if (coll.size() == 0) {
                            buf.write("Empty list");
                        } else {
                            ArrayList pds = null;
                            ArrayList listviewData = null;
                            Class lastClass = null;
                            Iterator it = coll.iterator();
                            while (it.hasNext()) {
                                Object collectionItem = it.next();
                                BeanInfo collectionBeanInfo = Introspector.getBeanInfo(collectionItem.getClass());
                                if (!collectionItem.getClass().equals(lastClass)) {
                                    if (listviewData != null && listviewData.size() > 0) renderBeanCollectionSegment(pds, listviewData, new PrintWriter(buf), lastClass);
                                    pds = new ArrayList();
                                    listviewData = new ArrayList();
                                    for (int i = 0; i < collectionBeanInfo.getPropertyDescriptors().length; i++) {
                                        PropertyDescriptor collectionPropertyDescriptor = collectionBeanInfo.getPropertyDescriptors()[i];
                                        if ((collectionPropertyDescriptor.getReadMethod() != null && collectionPropertyDescriptor.getReadMethod().getParameterTypes().length == 0) && ((collectionPropertyDescriptor.getReadMethod() != null && collectionPropertyDescriptor.getWriteMethod() == null) || (collectionPropertyDescriptor.getReadMethod() != null && collectionPropertyDescriptor.getWriteMethod() != null))) {
                                            pds.add(collectionPropertyDescriptor);
                                        }
                                    }
                                }
                                Object[] row = new Object[pds.size()];
                                for (int i = 0; i < pds.size(); i++) {
                                    PropertyDescriptor collectionPropertyDescriptor = (PropertyDescriptor) pds.get(i);
                                    StringBuffer tmp = new StringBuffer();
                                    tmp.append(formParameterName);
                                    tmp.append('.');
                                    tmp.append(i);
                                    tmp.append('.');
                                    String colletionItemFormParameterName = tmp.toString();
                                    row[i] = BeanForm.renderPropertyDescriptorEditor(request, response, colletionItemFormParameterName, collectionPropertyDescriptor, collectionItem);
                                }
                                listviewData.add(row);
                                lastClass = collectionItem.getClass();
                            }
                            if (listviewData != null && listviewData.size() > 0) renderBeanCollectionSegment(pds, listviewData, new PrintWriter(buf), lastClass);
                        }
                    } else {
                        if (obj == null) {
                            buf.write("<b>null</b> ");
                            if (pd.getWriteMethod() != null) {
                                buf.write("<input name='");
                                buf.write(formParameterName + "_newInstance' type='checkbox'> create new instance.");
                            }
                        } else {
                            buf.write("<input type='checkbox' name='");
                            buf.write(formParameterName);
                            buf.write("_maximized'");
                            if ("on".equalsIgnoreCase(request.getParameter(formParameterName + "_maximized"))) {
                                buf.write(" checked> maximized<br>");
                                BeanForm subForm = new BeanForm(formParameterName + ".", obj);
                                if (subForm.renderBeanForm(request, response, new PrintWriter(buf))) {
                                    pd.getWriteMethod().invoke(bean, new Object[] { subForm.getBean() });
                                }
                            } else {
                                buf.write("> maximized<br>");
                            }
                            if (pd.getReadMethod() != null && pd.getWriteMethod() == null) {
                                buf.write("<b>read only<b>");
                            } else if (pd.getWriteMethod() != null && obj == null) {
                            }
                        }
                    }
                    return buf.toString();
                } else {
                    return "parameters required";
                }
            } catch (Exception e) {
                StringWriter out = new StringWriter();
                out.write("<pre>");
                e.printStackTrace(new PrintWriter(out));
                out.write("</pre>");
                return out.toString();
            }
        } else {
            return "no reader method";
        }
    }
}
