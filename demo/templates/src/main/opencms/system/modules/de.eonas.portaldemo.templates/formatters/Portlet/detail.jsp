<%@page buffer="none" session="false" taglibs="c,cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>

<cms:formatter var="content" val="value">

    <div class="portlet box box_schema3">
        <!-- Portlet -->

        <c:set var="ID" value="${value.WindowID}"/>
        <c:set var="Portlet" value="${value.Portlet}"/>
        <h1>Portlet ${Portlet}${ID}</h1>
        <%
            String id = null;
            Object idobject = pageContext.getAttribute("ID");
            if (idobject != null) id = idobject.toString();

            String portlet = null;
            Object portletObject = pageContext.getAttribute("Portlet");
            if (portletObject != null) portlet = portletObject.toString();

            if (id != null && id.length() > 0 && portlet != null && portlet.length() > 0) {
                de.eonas.website.taglib.PortletTag tag = new de.eonas.website.taglib.PortletTag();
                tag.setPortletId(portlet + "" + id);
                tag.setPageContext(pageContext);
                tag.doStartTag();
        %>
        <!-- Window State Controls -->
        <h4>
            <%
                de.eonas.website.taglib.PortletTitleTag title = new de.eonas.website.taglib.PortletTitleTag();
                title.setPageContext(pageContext);
                title.setParent(tag);
                title.doStartTag();
                title.doEndTag();

            %>

            <span style="display: inline-block;">
                        <%
                de.eonas.website.taglib.PortletModeDropDownTag mode = new de.eonas.website.taglib.PortletModeDropDownTag();
                mode.setPageContext(pageContext);
                mode.setParent(tag);
                mode.setStyleClass("");
                mode.doStartTag();
                mode.doEndTag();
            %>
            </span>
        </h4>

        <div class="boxbody">
            <%
                de.eonas.website.taglib.PortletRenderTag render = new de.eonas.website.taglib.PortletRenderTag();
                render.setPageContext(pageContext);
                render.setParent(tag);
                render.doStartTag();
                render.doEndTag();
            %>
        </div>


        <%


                tag.doEndTag();

                pageContext.removeAttribute("javax.servlet.jsp.jstl.fmt.localizationContext.request");
                pageContext.removeAttribute("javax.servlet.include.servlet_path");
            }
        %>
    </div>

</cms:formatter>
