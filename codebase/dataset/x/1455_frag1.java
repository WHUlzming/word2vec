    public void cosRegisterManagedProcess(java.lang.String entity, tcg.syscontrol.cos.CosProcessTypeEnum p_processType, tcg.syscontrol.cos.ICosManagedProcess p_managedProcess, long p_processId) throws tcg.syscontrol.cos.CosProcessRunningException, tcg.syscontrol.cos.CosFailedToRegisterException {

        while (true) {

            if (!this._is_local()) {

                org.omg.CORBA.portable.InputStream _is = null;

                try {

                    org.omg.CORBA.portable.OutputStream _os = _request("cosRegisterManagedProcess", true);

                    _os.write_string(entity);

                    tcg.syscontrol.cos.CosProcessTypeEnumHelper.write(_os, p_processType);

                    tcg.syscontrol.cos.ICosManagedProcessHelper.write(_os, p_managedProcess);

                    _os.write_longlong(p_processId);

                    _is = _invoke(_os);

                    return;

                } catch (org.omg.CORBA.portable.RemarshalException _rx) {

                } catch (org.omg.CORBA.portable.ApplicationException _ax) {

                    String _id = _ax.getId();

                    if (_id.equals("IDL:tcg/syscontrol/cos/CosProcessRunningException:1.0")) {

                        throw tcg.syscontrol.cos.CosProcessRunningExceptionHelper.read(_ax.getInputStream());

                    } else if (_id.equals("IDL:tcg/syscontrol/cos/CosFailedToRegisterException:1.0")) {

                        throw tcg.syscontrol.cos.CosFailedToRegisterExceptionHelper.read(_ax.getInputStream());

                    }

                    throw new RuntimeException("Unexpected exception " + _id);

                } finally {

                    this._releaseReply(_is);

                }

            } else {

                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("cosRegisterManagedProcess", _opsClass);

                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");

                ICosProcessManagerOperations _localServant = (ICosProcessManagerOperations) _so.servant;

                try {

                    _localServant.cosRegisterManagedProcess(entity, p_processType, p_managedProcess, p_processId);

                } finally {

                    _servant_postinvoke(_so);

                }

                return;

            }

        }

    }
