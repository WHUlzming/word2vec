        String onmouseup = (String) selector.getOnmouseup();

        if (onmouseup != null) {

            rw.writeAttribute("onmouseup", onmouseup, "onmouseup");

        }

        String onselect = (String) selector.getOnselect();

        if (onselect != null) {

            rw.writeAttribute("onselect", onselect, "onselect");

        }

        boolean readonly = selector.getReadonly();
