        return area.isSomethingSelected();

    }



    public boolean isSelected(double x, double y) {

        return area.isSelected(x, y);

    }



    /**

	*	operate this channel

	*/

    public void operateChannel(AOperation o) {

        o.startOperation();

        if (isSelected()) {

            o.operate(this);

        }

        o.endOperation();
