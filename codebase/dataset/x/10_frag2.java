                        schemaManager.addSchemaObject(type);

                        break;

                    } catch (HsqlException e) {

                        return Result.newErrorResult(e, sql);

                    }

                }

            case StatementTypes.CREATE_TABLE:

                {

                    Table table = (Table) arguments[0];

                    HsqlArrayList tempConstraints = (HsqlArrayList) arguments[1];
