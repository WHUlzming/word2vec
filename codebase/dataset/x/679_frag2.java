        w.write("\n /**\n" + "  * Sets the <code>" + suffix + "</code> index value, with checking, for this " + "<code>Persistent</code>.\n" + ((description != null) ? "  * Field description: \n" + DSD.javadocFormat(2, 3, description) + "  * \n" : "") + "  * \n" + "  * @generator " + "org.melati.poem.prepro.SearchabiltyFieldDef" + "#generateBaseMethods \n" + "  * @param raw  the value to set \n" + "  * @throws AccessPoemException \n" + "  *         if the current <code>AccessToken</code> \n" + "  *         does not confer write access rights\n" + "  */\n");

        w.write("  public void set" + suffix + "Index(Integer raw)\n" + "      throws AccessPoemException {\n" + "    " + tableAccessorMethod + "().get" + suffix + "Column()." + "getType().assertValidRaw(raw);\n" + "    writeLock();\n" + "    set" + suffix + "_unsafe(raw);\n" + "  }\n" + "\n");

        w.write("\n /**\n" + "  * Retrieves the " + suffix + " value \n" + "  * of this <code>Persistent</code>.\n" + ((description != null) ? "  * Field description: \n" + DSD.javadocFormat(2, 3, description) + "  * \n" : "") + "  *\n" + "  * @generator " + "org.melati.poem.prepro.SearchabiltyFieldDef" + "#generateBaseMethods \n" + "  * @throws AccessPoemException \n" + "  *         if the current <code>AccessToken</code> \n" + "  *         does not confer read access rights\n" + "  * @return the " + type + "\n" + "  */\n");

        w.write("  public " + type + " get" + suffix + "()\n" + "      throws AccessPoemException {\n" + "    Integer index = get" + suffix + "Index();\n" + "    return index == null ? null :\n" + "        Searchability.forIndex(index.intValue());\n" + "  }\n" + "\n");
