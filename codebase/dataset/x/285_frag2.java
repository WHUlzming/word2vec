                JFileChooser chooser = new JFileChooser();

                chooser.setDialogTitle(RasterToolsUtil.getText(this, "guardar_tabla"));

                int returnVal = chooser.showOpenDialog(getHistogramPanel());

                if (returnVal == JFileChooser.APPROVE_OPTION) {

                    fName = chooser.getSelectedFile().toString();
