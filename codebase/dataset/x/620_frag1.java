                out.write("    }\n");

                out.write("    else { // Others: Firefox, Safari\n");

                out.write("      var ev = document.createEvent(\"HTMLEvents\");\n");

                out.write("      ev.initEvent(\"change\", true, true);\n");

                out.write("      _dynarch_popupCalendar.sel.dispatchEvent(ev);\n");

                out.write("    }\n");

                out.write("  }\n");

                out.write("//  cal.destroy();\n");

                out.write("  _dynarch_popupCalendar = null;\n");

                out.write("}\n");

                out.write("\n");

                out.write("// This function shows the calendar under the element having the given id.\n");

                out.write("// It takes care of catching \"mousedown\" signals on document and hiding the\n");

                out.write("// calendar if the click was outside.\n");

                out.write("function showCalendar(id, format, showsTime, showsOtherMonths) {\n");

                out.write("  var el = document.getElementById(id);\n");

                out.write("  if (_dynarch_popupCalendar != null) {\n");

                out.write("    // we already have some calendar created\n");
