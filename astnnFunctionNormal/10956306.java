class BackupThread extends Thread {
    private void runUnitTest(UnitTest unittest, int count) {
        String className = new String();
        String methodName = new String();
        try {
            className = unittest.getClassName();
            Class clazz = Class.forName(className);
            Class tclass = Class.forName("junit.framework.TestCase");
            Class iclass = Class.forName("org.jaffa.tools.loadtest.ILoadTestCase");
            String param = className + ".class";
            Object obj = clazz.getConstructor(new Class[] { String.class }).newInstance(new Object[] { param });
            methodName = unittest.getMethodName();
            Method method = clazz.getDeclaredMethod(methodName, (Class[]) null);
            if (iclass.isInstance(obj)) {
                String setWC = "setWebConversation";
                String setResp = "setWebResponse";
                Method setWcMethod = clazz.getMethod(setWC, new Class[] { WebConversation.class });
                Method setRespMethod = clazz.getMethod(setResp, new Class[] { WebResponse.class });
                setWcMethod.invoke(obj, new Object[] { this.conversation });
                setRespMethod.invoke(obj, new Object[] { this.loggedOn });
            }
            long startTime = System.currentTimeMillis();
            DateTime startdt = new DateTime(startTime);
            method.invoke(obj, (Object[]) null);
            long endTime = System.currentTimeMillis();
            DateTime enddt = new DateTime(endTime);
            long duration = endTime - startTime;
            synchronized (this) {
                writer.output(threadNumber, count, startdt.toString(), enddt.toString(), Long.toString(duration), className + "." + methodName, "Success");
            }
            Thread.sleep(m_testSet.getRunDelay().intValue());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            log.error("No such Method - " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.error("Can't find Class - " + e.getMessage());
        } catch (InvocationTargetException e) {
            synchronized (this) {
                writer.output(threadNumber, count, "", "", "", className + "." + methodName, "Failure");
            }
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
