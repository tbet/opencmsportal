<%@page buffer="none" session="false" taglibs="c,cms" %>

<cms:formatter var="content" val="value">

<div class="portlet">
  <c:forEach var="portlet" items="${content.valueList.Portlet}">
    <!-- Portlet -->
    <c:set var="ID" value="${portlet.value.ID}"/>
      <%
          String id = null;
          Object idobject = pageContext.getAttribute("ID");
          if (idobject != null) id = idobject.toString();

          if (id != null && id.length() > 0) {
              de.eonas.website.taglib.PortletTag tag = new de.eonas.website.taglib.PortletTag();
              tag.setPortletId(id);
              tag.setPageContext(pageContext);
              tag.doStartTag();
      %>
      <!-- Window State Controls -->
      <div class="ui-portlet ui-widget ui-widget-content ui-corner-all">
          <div class="ui-portlet-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
			<span class="ui-portlet-title" id="ui-portlet-title-dialog">
			<%
                de.eonas.website.taglib.PortletTitleTag title = new de.eonas.website.taglib.PortletTitleTag();
                title.setPageContext(pageContext);
                title.setParent(tag);
                title.doStartTag();
                title.doEndTag();

            %>
			</span>
              <%
                  de.eonas.website.taglib.PortletModeDropDownTag mode = new de.eonas.website.taglib.PortletModeDropDownTag();
                  mode.setPageContext(pageContext);
                  mode.setParent(tag);
                  mode.setStyleClass("");
                  mode.doStartTag();
                  mode.doEndTag();
              %>
          </div>

          <div class="ui-portlet-content ui-widget-content" style="width: auto; min-height: 60.4px; height: auto;"
               scrolltop="0" scrollleft="0">
              <%
                  de.eonas.website.taglib.PortletRenderTag render = new de.eonas.website.taglib.PortletRenderTag();
                  render.setPageContext(pageContext);
                  render.setParent(tag);
                  render.doStartTag();
                  render.doEndTag();
              %>
          </div>
      </div>

      <%


              tag.doEndTag();

              pageContext.removeAttribute("javax.servlet.jsp.jstl.fmt.localizationContext.request");
              pageContext.removeAttribute("javax.servlet.include.servlet_path");
          }
      %>
  </c:forEach>
</div>

</cms:formatter>
