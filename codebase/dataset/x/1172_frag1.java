                            dimWrite = lg_ptr_init2.length - timedData[i].x * timedData[i].y;

                        } else {

                            lg_ptr_read2 = new Long[timedData[i].x];

                            dimWrite = lg_ptr_init2.length - timedData[i].x;

                            if (dimWrite > dbData_w.getMax_x()) {

                                dbData_w.setMax_x(dimWrite);

                            }

                        }

                        if (dimWrite < 0) {

                            dimWrite = 0;

                        }

                        lg_ptr_write2 = new Long[dimWrite];

                        if (dataFormat == AttrDataFormat._IMAGE) {

                            for (int j = 0; j < timedData[i].x * timedData[i].y; j++) {

                                lg_ptr_read2[j] = lg_ptr_init2[j];

                            }

                            for (int j = timedData[i].x * timedData[i].y; j < lg_ptr_init2.length; j++) {

                                lg_ptr_write2[j - timedData[i].x] = lg_ptr_init2[j];

                            }

                        } else {

                            for (int j = 0; j < timedData[i].x; j++) {
