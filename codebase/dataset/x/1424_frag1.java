        String xml = "<show name=\"test\">\n" + "  <fixtures>\n" + "    <fixture id=\"1\" name=\"Channel 1\"/>\n" + "    <fixture id=\"2\" name=\"Channel 2\"/>\n" + "    <fixture id=\"3\" name=\"Channel 3\"/>\n" + "  </fixtures>\n" + "  <submasters>\n" + "    <submaster id=\"1\" name=\"Submaster 1\"/>\n" + "    <submaster id=\"2\" name=\"Submaster 2\">\n" + "      <scene>\n" + "        <set fixture-id=\"1\" attribute=\"Intensity\" value=\"100\"/>\n" + "        <set fixture-id=\"2\" attribute=\"Intensity\" value=\"50\"/>\n" + "      </scene>\n" + "    </submaster>\n" + "  </submasters>\n" + "  <cue-lists>\n" + "    <cue-list id=\"1\" name=\"Cue list 1\">\n" + "      <comment>\n" + "        <line text=\"cuelist comment line 1\"/>\n" + "        <line text=\"cuelist comment line 2\"/>\n" + "      </comment>\n" + "      <cue number=\"1.1\" page=\"page\" prompt=\"prompt\" description=\"L 2\">\n" + "        <comment>\n" + "          <line text=\"cue comment line 1\"/>\n" + "          <line text=\"cue comment line 2\"/>\n" + "        </comment>\n" + "        <submasters>\n" + "          <submaster id=\"1\" value=\"derived\"/>\n" + "          <submaster id=\"2\" value=\"77\"/>\n" + "        </submasters>\n" + "        <scene>\n" + "          <set fixture-id=\"1\" attribute=\"Intensity\" value=\"50\"/>\n" + "          <set fixture-id=\"2\" attribute=\"Intensity\" value=\"derived\"/>\n" + "        </scene>\n" + "      </cue>\n" + "    </cue-list>\n" + "  </cue-lists>\n" + "</show>\n";
