                        b.putInt("site_count", 0);

                        msg.setData(b);

                        mHandler.sendMessage(msg);

                    }

                } else {

                    msg = mHandler.obtainMessage();

                    b = new Bundle();

                    b.putInt("percent_done", -5);

                    b.putInt("site_count", 0);

                    msg.setData(b);

                    mHandler.sendMessage(msg);
