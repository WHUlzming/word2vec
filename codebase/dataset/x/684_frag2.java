                    } catch (CannotProceed ex) {

                        out = rh.createExceptionReply();

                        CannotProceedHelper.write(out, ex);

                    } catch (InvalidName ex) {

                        out = rh.createExceptionReply();

                        InvalidNameHelper.write(out, ex);

                    } catch (AlreadyBound ex) {

                        out = rh.createExceptionReply();

                        AlreadyBoundHelper.write(out, ex);

                    }

                    break;

                }
