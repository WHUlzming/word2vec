        name = doc.createElement("start");

        int hour = start.get(Calendar.HOUR_OF_DAY);

        if (hour > 12) hour = hour - 12; else if (hour == 0) hour = 12;

        name.setAttribute("hour_12", intToXchar(hour, 2));

        name.setAttribute("hour_24", intToXchar(start.get(Calendar.HOUR_OF_DAY), 2));

        name.setAttribute("minute", intToXchar(start.get(Calendar.MINUTE), 2));

        if (start.get(Calendar.AM_PM) == Calendar.AM) name.setAttribute("am_pm", "am"); else name.setAttribute("am_pm", "pm");

        runnintItem.appendChild(name);

        name = doc.createElement("id");
