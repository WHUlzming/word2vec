                        case 'd':

                            X = "fixed";

                            id = Id_fixed;

                            break L;

                        case 'e':

                            X = "slice";

                            id = Id_slice;

                            break L;

                        case 'h':

                            X = "match";

                            id = Id_match;

                            break L;

                        case 'k':

                            X = "blink";

                            id = Id_blink;

                            break L;

                        case 'l':

                            X = "small";

                            id = Id_small;

                            break L;

                        case 't':

                            X = "split";

                            id = Id_split;

                            break L;

                    }

                    break L;

                case 6:

                    switch(s.charAt(1)) {

                        case 'e':

                            X = "search";

                            id = Id_search;

                            break L;

                        case 'h':

                            X = "charAt";

                            id = Id_charAt;

                            break L;

                        case 'n':

                            X = "anchor";

                            id = Id_anchor;

                            break L;

                        case 'o':

                            X = "concat";

                            id = Id_concat;

                            break L;

                        case 'q':
