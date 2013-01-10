<%@page buffer="none" session="false" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<!DOCTYPE html>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]> <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]> <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js"> <!--<![endif]-->
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><cms:info property="opencms.title"/></title>
    <cms:enable-ade/>

    <meta name="description" content="">

    <meta name="viewport" content="width=device-width">

    <link rel="stylesheet" href="<cms:link>../resources/css/bootstrap.min.css</cms:link>">
    <style>
        body {
            padding-top: 60px;
            padding-bottom: 40px;
        }
    </style>
    <link rel="stylesheet" href="<cms:link>../resources/css/bootstrap-responsive.min.css</cms:link>">
    <link rel="stylesheet" href="<cms:link>../resources/css/main.css</cms:link>">
    <link rel="stylesheet" href="<cms:link>../resources/css/docs.css</cms:link>">

    <script src="<cms:link>../resources/js/vendor/modernizr-2.6.2-respond-1.1.0.min.js</cms:link>"></script>

    <cms:headincludes type="javascript"/>
    <cms:headincludes type="css"/>

    <!-- Fav and touch icons -->
    <link rel="apple-touch-icon-precomposed" sizes="144x144"
          href="<cms:link>../resources/apple-touch-icon-144-precomposed.png</cms:link>">
    <link rel="apple-touch-icon-precomposed" sizes="114x114"
          href="<cms:link>../resources/apple-touch-icon-114-precomposed.png</cms:link>">
    <link rel="apple-touch-icon-precomposed" sizes="72x72"
          href="<cms:link>../resources/apple-touch-icon-72-precomposed.png</cms:link>">
    <link rel="apple-touch-icon-precomposed"
          href="<cms:link>../resources/apple-touch-icon-57-precomposed.png</cms:link>">
    <link rel="shortcut icon" href="../assets/ico/favicon.png">

</head>

<body data-spy="scroll" data-target=".bs-docs-sidebar">

<!--[if lt IE 7]>
<p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade
    your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to
    improve your experience.</p>
<![endif]-->

<cms:include file="menu/nav_top.jsp"/>

