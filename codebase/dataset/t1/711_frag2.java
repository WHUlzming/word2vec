                Font newFont = new Font(StyleEditionFrame.this.textPreviewLabel.getFont().getFamily(), StyleEditionFrame.this.textPreviewLabel.getFont().getStyle(), Integer.parseInt(((JComboBox) e.getSource()).getSelectedItem().toString()));

                StyleEditionFrame.this.textPreviewLabel.setFont(newFont);

                if (StyleEditionFrame.this.symbolizer != null) {

                    StyleEditionFrame.this.symbolizer.setFont(new fr.ign.cogit.geoxygene.style.Font(newFont));

                }

                StyleEditionFrame.this.layerLegendPanel.getModel().fireActionPerformed(null);
