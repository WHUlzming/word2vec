                            dimWrite = str_ptr_init.length - timedData[i].x * timedData[i].y;

                        } else {

                            str_ptr_read = new String[timedData[i].x];

                            dimWrite = str_ptr_init.length - timedData[i].x;

                            if (dimWrite > dbData_w.getMax_x()) {

                                dbData_w.setMax_x(dimWrite);

                            }

                        }

                        if (dimWrite < 0) {

                            dimWrite = 0;

                        }

                        str_ptr_write = new String[dimWrite];

                        if (dataFormat == AttrDataFormat._IMAGE) {

                            for (int j = 0; j < timedData[i].x * timedData[i].y; j++) {

                                str_ptr_read[j] = str_ptr_init[j];

                            }

                            for (int j = timedData[i].x * timedData[i].y; j < str_ptr_init.length; j++) {

                                str_ptr_write[j - timedData[i].x] = str_ptr_init[j];

                            }

                        } else {

                            for (int j = 0; j < timedData[i].x; j++) {
