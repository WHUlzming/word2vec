        VarComboBox(ComboBoxModel m, EnumVariableValue var) {

            super(m);

            _var = var;

            _l = new java.beans.PropertyChangeListener() {



                public void propertyChange(java.beans.PropertyChangeEvent e) {

                    if (log.isDebugEnabled()) log.debug("VarComboBox saw property change: " + e);

                    originalPropertyChanged(e);

                }

            };

            setBackground(_var._value.getBackground());

            _var.addPropertyChangeListener(_l);

        }
