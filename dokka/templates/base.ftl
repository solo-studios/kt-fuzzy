<#import "includes/page_metadata.ftl" as page_metadata>
<#import "includes/header.ftl" as header>
<#import "includes/footer.ftl" as footer>
<!DOCTYPE html>
<html class="no-js">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8">
    <@page_metadata.display/>
    <@template_cmd name="pathToRoot">
        <script>var pathToRoot = "${pathToRoot}";</script></@template_cmd>
    <script>document.documentElement.classList.replace("no-js", "js");</script>
    <#-- This script doesn't need to be there but it is nice to have
    since app in dark mode doesn't 'blink' (class is added before it is rendered) -->
    <script>const storage = localStorage.getItem("dokka-dark-mode")
        if (storage == null) {
            const osDarkSchemePreferred = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches
            if (osDarkSchemePreferred === true) {
                document.getElementsByTagName("html")[0].classList.add("theme-dark")
            }
        } else {
            const savedDarkMode = JSON.parse(storage)
            if (savedDarkMode === true) {
                document.getElementsByTagName("html")[0].classList.add("theme-dark")
            }
        }
    </script>
    <!-- MathJax configuration -->
    <script type="text/x-mathjax-config">
        MathJax.Hub.Config({
            menuSettings: {
                zoom: "Double-Click",
                zscale: "300%"
            },
            styles: {
                "#MathJax_Zoom": {
                    "background-color": "var(--background-color)",
                    "box-shadow": "5px 5px 15px var(--background-color)",
                    "-webkit-box-shadow": "5px 5px 15px var(--background-color)",
                    "-moz-box-shadow": "5px 5px 15px var(--background-color)",
                    "-khtml-box-shadow": "5px 5px 15px var(--background-color)",
                    "border": "1px solid var(--color-dark)"
                },
                ".MathJax": {}
            }
        })
    </script>
    <!-- MathJax script -->
    <#-- Resources (scripts, stylesheets) are handled by Dokka.
    Use customStyleSheets and customAssets to change them. -->
    <@resources/>
</head>
<body>
<div class="root match-braces">
    <@header.display/>
    <div id="container">
        <div class="sidebar" id="leftColumn">
            <div class="sidebar--inner" id="sideMenu"></div>
        </div>
        <div id="main">
            <@content/>
            <@footer.display/>
        </div>
    </div>
</div>
</body>
</html>
