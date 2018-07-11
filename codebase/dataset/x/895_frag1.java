    public void newPlot() {

        NewPlotDialog newPlotDialog = new NewPlotDialog();

        newPlotDialog.setVisible(true);

        if (newPlotDialog.getPressed() == NewPlotDialog.OK_PRESSED) {

            String plotType = newPlotDialog.getSelected();

            Plot newPlot = PlotFactory.getPlot(plotType);

            if (newPlot != null) {

                PlotView newView = new PlotView(newPlot);

                ViewManager.addView(newView);

            } else {

                System.out.println("Invalid plot type.");

            }

        }

    }
