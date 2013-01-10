<%@page buffer="none" session="false" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="navStartLevel"><cms:property name="NavStartLevel" file="search" default="0"/></c:set>
<cms:navigation type="forFolder" startLevel="0" var="nav" endLevel="${navStartLevel+1}"/>

<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
<!--            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>

                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <a class="brand" href="#"><cms:info property="opencms.title"/></a> -->

            <div class="nav-collapse collapse">
                <c:if test="${!empty nav.items}">
                    <ul class="nav">
                        <c:set var="oldLevel" value=""/>
                        <c:forEach items="${nav.items}" var="elem" varStatus="status">
                            <c:set var="currentLevel" value="${elem.navTreeLevel}"/>
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

                            <c:choose>
                                <c:when test="${nextLevel > currentLevel}">
                                    <li class="dropdown ${activecss}">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">${elem.navText}<b
                                            class="caret"></b></a>
                                    <ul class="dropdown-menu">
                                </c:when>
                                <c:otherwise>
                                    <li class="${activecss}"><a
                                            href="<cms:link>${elem.resourceName}</cms:link>">${elem.navText}</a></li>
                                    <c:if test="${nextLevel < currentLevel}">
                                        <c:forEach begin="${nextLevel+1}" end="${currentLevel}"></ul></li></c:forEach>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                    </ul>

                </c:if>
            </div>

            <form class="navbar-form pull-right">
                <input class="span2" type="text" placeholder="Username">
                <input class="span2" type="password" placeholder="Password">
                <button type="submit" class="btn">Sign in</button>
            </form>
        </div>
        <!--/.nav-collapse -->
    </div>
</div>

