                                            int l = (int) (f * l1 + (1 - f) * l0);

                                            int c = lut.colors[l];

                                            r = (c >> 16) & 0xff;

                                            g = (c >> 8) & 0xff;

                                            b = c & 0xff;

                                        }

                                        if (light > 0) {

                                            tr.x = p0.dx;

                                            tr.y = p0.dy;

                                            tr.z = p0.dz;

                                            tr.xyzPos_();

                                            float l = light * (-tr.X / (float) p0.len) + 1;

                                            r = (int) Math.min(255, l * r);

                                            g = (int) Math.min(255, l * g);

                                            b = (int) Math.min(255, l * b);

                                        }

                                        pixels[pos] = 0xff000000 | (r << 16) | (g << 8) | b;

                                    }

                                }

                            }

                        }
