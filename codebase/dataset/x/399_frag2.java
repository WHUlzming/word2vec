                if (tokenizer.ttype == EnhancedStreamTokenizer.TT_NUMBER) tx = (int) tokenizer.nval; else throw (new DBGParseException(errorString, tokenizer, fileName));

                tokenizer.nextToken();

                if (tokenizer.ttype == EnhancedStreamTokenizer.TT_NUMBER) ty = (int) tokenizer.nval; else throw (new DBGParseException(errorString, tokenizer, fileName));

                tokenizer.nextToken();

                if (tokenizer.ttype == EnhancedStreamTokenizer.TT_NUMBER) tx2 = (int) tokenizer.nval; else throw (new DBGParseException(errorString, tokenizer, fileName));

                tokenizer.nextToken();

                if (tokenizer.ttype == EnhancedStreamTokenizer.TT_NUMBER) ty2 = (int) tokenizer.nval; else throw (new DBGParseException(errorString, tokenizer, fileName));
