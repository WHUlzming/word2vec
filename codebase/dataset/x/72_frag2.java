            public void actionPerformed(java.awt.event.ActionEvent evt) {

                _flPane.includeInPrint(selectAll.isSelected());

                funct.setSelected(selectAll.isSelected());

                Enumeration<JCheckBox> en = printList.keys();

                while (en.hasMoreElements()) {

                    JCheckBox check = en.nextElement();

                    printList.get(check).includeInPrint(selectAll.isSelected());

                    check.setSelected(selectAll.isSelected());

                }

            }
