            subj = readInteraction.doGetByRequest(request, authUser);

        } catch (NoSuchContextException e) {

            logr.debug(e.toString());

            throw new IDBeanException(e);

        } catch (SchemaException e) {

            logr.debug(e.toString());

            throw new IDBeanException(e);

        } catch (MappingException e) {

            logr.debug(e.toString());

            throw new IDBeanException(e);

        } catch (DeclarationException e) {

            logr.debug(e.toString());

            throw new IDBeanException(e);

        }

        return subj;
