        public void actionPerformed(ActionEvent e) {

            String action = e.getActionCommand();

            if (action.equals("comboBoxChanged")) {

                JComboBox cb = (JComboBox) e.getSource();

                String test = ((JLabel) cb.getSelectedItem()).getName();

            }

            if (action.startsWith("Get")) {

                if (action.equals("Get Default Highlighter")) parent.fullCopy(defHigh);

                if (action.equals("Get Default Pen")) parent.fullCopy(defPen);

                if (action.equals("Get Current Pen")) parent.fullCopy(oldparent);

                if (action.equals("Get Button Pen")) parent.fullCopy(defBut);

                setDialog();

            }

            if (action.startsWith("Set")) {

                JLabel jl = (JLabel) combo2.getSelectedItem();

                color = jl.getName();

                String test = ((JLabel) combo3.getSelectedItem()).getName();

                highlighter = false;

                transparency = 255;

                if (test.equals("Translucent Highlighter")) {

                    setTranslucent();

                    if (action.equals("Set Default")) highlighterStyle = "translucent";

                }

                if (test.equals("Transparent Highlighter")) {

                    setTransparent();

                    if (action.equals("Set Default")) highlighterStyle = "transparent";

                }

                if (test.equals("Bottom Highlighter")) {

                    highlighter = true;

                    if (action.equals("Set Default")) highlighterStyle = "bottom";

                }

                float x = model2.getNumber().floatValue();

                defHigh.fatWidth = x;

                defPen.fatWidth = x;

                curPen.fatWidth = x;

                defBut.fatWidth = x;

                parent.fatWidth = x;

                x = model1.getNumber().floatValue();

                defHigh.bWidth = x;

                defPen.bWidth = x;

                curPen.bWidth = x;

                defBut.bWidth = x;

                parent.bWidth = x;

                x = model4.getNumber().floatValue();

                defHigh.hTrans = x;

                defPen.hTrans = x;

                curPen.hTrans = x;

                defBut.hTrans = x;

                parent.hTrans = x;

                jarn.markerweight = model3.getNumber().intValue();

                setWidth(((JLabel) combo1.getSelectedItem()).getName());

                boolean setHigh = false;

                if (highlighter || (transparency != 255)) setHigh = true;

                if (action.equals("Set Default")) {

                    if (setHigh) defHigh.fullCopy(parent); else defPen.fullCopy(parent);

                }

                if (action.equals("Set Button Pen")) defBut.fullCopy(parent);

                if (action.equals("Set Current Pen")) curPen.fullCopy(parent);

                defHigh.setWidth(defHigh.type);

                defPen.setWidth(defPen.type);

                defBut.setWidth(defBut.type);

                curPen.setWidth(curPen.type);

                jw.setVisible(false);

                dirty = true;

                done = true;

            }

            if (action.equals("Cancel")) {

                jw.setVisible(false);

                dirty = false;

                done = true;

            }

        }
