                                this.line = accept_line;

                                return token;

                            }

                        case 11:

                            {

                                @SuppressWarnings("hiding") Token token = new11(start_line + 1, start_pos + 1);

                                pushBack(accept_length);

                                this.pos = accept_pos;
