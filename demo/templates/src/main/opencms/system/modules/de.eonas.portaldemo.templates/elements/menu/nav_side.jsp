<%@page buffer="none" session="false" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="navStartLevel"><cms:property name="NavStartLevel" file="search" default="0"/></c:set>
<cms:navigation type="treeForFolder" startLevel="1" var="nav" endLevel="2"/>

<c:if test="${!empty nav.items}">
    <div class="bs-docs-sidebar">
        <ul class="nav nav-list bs-docs-sidenav">

            <c:forEach items="${nav.items}" var="elem" varStatus="status">
                <c:set var="activecss"><c:if
                        test="${fn:startsWith(cms.requestContext.uri, elem.resourceName)}">active</c:if></c:set>

                <c:choose>
                    <c:when test="${!status.last}">
                        <c:set var="nextLevel" value="${nav.items[status.index+1].navTreeLevel}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="nextLevel" value="${navStartLevel}"/>
                    </c:otherwise>
                </c:choose>

                <li class="${activecss}">
                    <a href="<cms:link>${elem.resourceName}</cms:link>">
                        <i class="icon-chevron-right"></i>
                            ${elem.navText}
                    </a>
                </li>
            </c:forEach>
        </ul>
    </div>
</c:if>
