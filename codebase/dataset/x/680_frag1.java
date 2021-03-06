            writeData("	display: none;");

            writeData("}");

            writeData("* html .hnav ul li, * html .hnav ul li a");

            writeData("{");

            writeData("	width: 1%; /* IE/Mac needs this */");

            writeData("	display: inline-block;	/* IE/Mac needs this */");

            writeData("		width: auto;");

            writeData("		display: inline;");

            writeData("	/* reset above hack */");

            writeData("}");

            writeData("* html .hnav, * html .hnav ul a");

            writeData("{");

            writeData("	height: 0.01%; /* hasLayout hack to fix render bugs in IE/Win. ");

            writeData("				 IE/Mac will ignore this rule. */");

            writeData("}");

            writeData("* html .HNAV");

            writeData("{");

            writeData("	padding: 0;	/* IE5/Win will resize #hnav to fit the heights of its");

            writeData("			   inline children that have vertical padding. So this");

            writeData("			   incorrect case selector hack will be applied only by");

            writeData("			   IE 5.x/Win */");

            writeData("}");

            writeData("/* everything below this point is related to the page's \"theme\" and could be");

            writeData(" * placed in a separate stylesheet to allow for multiple color/font scemes on");

            writeData(" * the layout. you should probably leave a default theme within this stylesheet");

            writeData(" * just to be on the safe side.	");

            writeData(" */");

            writeData("#pageWrapper, #masthead, #innerColumnContainer, #footer, .vnav ul, .vnav ul li, .hnav, .hnav ul li a");

            writeData("{");

            writeData("	border-color: #565;");

            writeData("}");

            writeData("html, body");

            writeData("{");

            writeData("	/* note that both html and body elements are in the selector.");

            writeData("	 * this is because we have margins applied to the body element");

            writeData("	 * and the HTML's background property will show through if");

            writeData("	 * it is ever set. _DO_NOT_ apply a font-size value to the");

            writeData("	 * html or body elements, set it in #pageWrapper.");

            writeData("	 */");

            writeData("	background-color: #eee;");

            writeData("	color: #000;");

            writeData("    	font-family: Verdana, Arial, Tahoma;");

            writeData("    	font-size: 14px; ");

            writeData("    	margin:0px;");

            writeData("	");

            writeData("}");

            writeData("#pageWrapper");

            writeData("{");

            writeData("	font-size: 80%;	/* set your default font size here. */");

            writeData("}");

            writeData("#masthead");

            writeData("{");

            writeData("	background-color: #4411A1;");

            writeData("	color: #fff;");

            writeData("}");

            writeData("#bannerOuterWrapper {");

            writeData("	height:100px;");

            writeData("	background-color: #fff;");

            writeData("}");

            writeData("#bannerWrapper {");

            writeData("	height:100px;");

            writeData("}");

            writeData("#bannerWrapperRepeat {");

            writeData("	height:100px;");

            writeData("}");

            writeData(".logo_text {");

            writeData("	font-size: 220%;");

            writeData("	font-weight:bold;");

            writeData("	color:#CCCCFF;");

            writeData("}");

            writeData(".logo_body_text {");

            writeData("	font-size: 100%;");

            writeData("	font-weight:bold;");

            writeData("	color:#4411A1;");

            writeData("}");

            writeData(".hnav");

            writeData("{");
