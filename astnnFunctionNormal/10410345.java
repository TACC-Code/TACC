class BackupThread extends Thread {
    private void addFormsListeners() {
        commonValues.setFormsChanged(false);
        boolean showMessage = false;
        String formsFlag = System.getProperty("listenForms");
        if (formsFlag != null) showMessage = true;
        org.jpedal.objects.acroforms.AcroRenderer formRenderer = decode_pdf.getCurrentFormRenderer();
        if (formRenderer == null) return;
        java.util.List formsOnPage = null;
        try {
            formsOnPage = formRenderer.getComponentNameList(commonValues.getCurrentPage());
        } catch (PdfException e) {
            LogWriter.writeLog("Exception " + e + " reading component list");
        }
        if (formsOnPage == null) {
            if (showMessage) showMessageDialog(Messages.getMessage("PdfViewer.NoFields"));
            return;
        }
        int formCount = formsOnPage.size();
        JPanel formPanel = new JPanel();
        if (showMessage) {
            formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
            JLabel formHeader = new JLabel("This page contains " + formCount + " form objects");
            formHeader.setFont(headFont);
            formPanel.add(formHeader);
            formPanel.add(Box.createRigidArea(new Dimension(10, 10)));
            JTextPane instructions = new JTextPane();
            instructions.setPreferredSize(new Dimension(450, 180));
            instructions.setEditable(false);
            instructions.setText("This provides a simple example of Forms handling. We have" + " added a listener to each form so clicking on it shows the form name.\n\n" + "Code is in addExampleListeners() in org.examples.simpleviewer.SimpleViewer\n\n" + "This could be easily be extended to interface with a database directly " + "or collect results on an action and write back using itext.\n\n" + "Forms have been converted into Swing components and are directly accessible" + " (as is the original data).\n\n" + "If you don't like the standard SwingSet you can replace with your own set.");
            instructions.setFont(textFont);
            formPanel.add(instructions);
            formPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        }
        for (int i = 0; i < formCount; i++) {
            String formName = (String) formsOnPage.get(i);
            Component[] comp = formRenderer.getComponentsByName(formName);
            Integer pageKey = new Integer(i);
            if (comp != null && pagesDecoded.get(pageKey) == null) {
                pagesDecoded.put(pageKey, "x");
                int count = comp.length;
                for (int index = 0; index < count; index++) {
                    if ((showMessage) && (index == 0)) {
                        JLabel type = new JLabel();
                        JLabel label = new JLabel("Form name=" + formName);
                        String labelS = "type=" + comp[index].getClass();
                        if (count > 1) {
                            labelS = "Group of " + count + " Objects, type=" + comp[index].getClass();
                            type.setForeground(Color.red);
                        }
                        type.setText(labelS);
                        label.setFont(headFont);
                        type.setFont(textFont);
                        formPanel.add(label);
                        formPanel.add(type);
                        formPanel.add(new JLabel(" "));
                    }
                    FormActionListener changeList = new FormActionListener(formName + index, frame, showMessage);
                    if (comp[index] instanceof JComboBox) {
                        ((JComboBox) comp[index]).addActionListener(changeList);
                    } else if (comp[index] instanceof JCheckBox) {
                        ((JCheckBox) comp[index]).addActionListener(changeList);
                    } else if (comp[index] instanceof JRadioButton) {
                        ((JRadioButton) comp[index]).addActionListener(changeList);
                    } else if (comp[index] instanceof JTextField) {
                        ((JTextField) comp[index]).addActionListener(changeList);
                    }
                }
            }
        }
        if (showMessage) {
            final JDialog displayFrame = new JDialog(frame, true);
            if (commonValues.getModeOfOperation() != Values.RUNNING_APPLET) {
                displayFrame.setLocationRelativeTo(null);
                displayFrame.setLocation(frame.getLocationOnScreen().x + 10, frame.getLocationOnScreen().y + 10);
            }
            JScrollPane scroll = new JScrollPane();
            scroll.getViewport().add(formPanel);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            displayFrame.setSize(500, 500);
            displayFrame.setTitle("List of forms on this page");
            displayFrame.getContentPane().setLayout(new BorderLayout());
            displayFrame.getContentPane().add(scroll, BorderLayout.CENTER);
            JPanel buttonBar = new JPanel();
            buttonBar.setLayout(new BorderLayout());
            displayFrame.getContentPane().add(buttonBar, BorderLayout.SOUTH);
            JButton no = new JButton(Messages.getMessage("PdfViewerButton.Close"));
            no.setFont(new Font("SansSerif", Font.PLAIN, 12));
            buttonBar.add(no, BorderLayout.EAST);
            no.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    displayFrame.dispose();
                }
            });
            displayFrame.show();
        }
    }
}
