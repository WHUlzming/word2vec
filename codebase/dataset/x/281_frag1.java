    private boolean _jspx_meth_html_005fselect_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context) throws Throwable {

        PageContext pageContext = _jspx_page_context;

        JspWriter out = _jspx_page_context.getOut();

        org.apache.struts.taglib.html.SelectTag _jspx_th_html_005fselect_005f0 = (org.apache.struts.taglib.html.SelectTag) _005fjspx_005ftagPool_005fhtml_005fselect_005fstyleId_005fstyleClass_005fproperty.get(org.apache.struts.taglib.html.SelectTag.class);

        _jspx_th_html_005fselect_005f0.setPageContext(_jspx_page_context);

        _jspx_th_html_005fselect_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);

        _jspx_th_html_005fselect_005f0.setProperty("tipoImpressao");

        _jspx_th_html_005fselect_005f0.setStyleId("tipoImpressao");

        _jspx_th_html_005fselect_005f0.setStyleClass("campo");

        int _jspx_eval_html_005fselect_005f0 = _jspx_th_html_005fselect_005f0.doStartTag();

        if (_jspx_eval_html_005fselect_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {

            if (_jspx_eval_html_005fselect_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {

                out = _jspx_page_context.pushBody();

                _jspx_th_html_005fselect_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);

                _jspx_th_html_005fselect_005f0.doInitBody();

            }

            do {

                out.write("\r\n");

                out.write("\t\t\t");

                if (_jspx_meth_html_005foption_005f0(_jspx_th_html_005fselect_005f0, _jspx_page_context)) return true;

                out.write("\r\n");

                out.write("\t\t\t");

                if (_jspx_meth_html_005foptionsCollection_005f0(_jspx_th_html_005fselect_005f0, _jspx_page_context)) return true;

                out.write("\r\n");

                out.write("\t\t");

                int evalDoAfterBody = _jspx_th_html_005fselect_005f0.doAfterBody();

                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;

            } while (true);

            if (_jspx_eval_html_005fselect_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {

                out = _jspx_page_context.popBody();

            }

        }

        if (_jspx_th_html_005fselect_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {

            _005fjspx_005ftagPool_005fhtml_005fselect_005fstyleId_005fstyleClass_005fproperty.reuse(_jspx_th_html_005fselect_005f0);

            return true;

        }

        _005fjspx_005ftagPool_005fhtml_005fselect_005fstyleId_005fstyleClass_005fproperty.reuse(_jspx_th_html_005fselect_005f0);

        return false;

    }
