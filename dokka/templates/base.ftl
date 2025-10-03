<#import "includes/page_metadata.ftl" as page_metadata>
<#import "includes/header.ftl" as header>
<#import "includes/footer.ftl" as footer>
<!DOCTYPE html>
<html class="no-js" lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8">
    <@page_metadata.display/>
    <@template_cmd name="pathToRoot">
        <script>var pathToRoot = "${pathToRoot}";</script>
    </@template_cmd>
    <script>document.documentElement.classList.replace("no-js", "js");</script>
    <#-- This script doesn't need to be there but it is nice to have
    since app in dark mode doesn't 'blink' (class is added before it is rendered) -->
    <script>
        const storage = localStorage.getItem("dokka-dark-mode");
        if (storage == null) {
            const osDarkSchemePreferred = window.matchMedia && window.matchMedia("(prefers-color-scheme: dark)").matches;
            if (osDarkSchemePreferred === true) {
                document.getElementsByTagName("html")[0].classList.add("theme-dark");
            }
        } else {
            const savedDarkMode = JSON.parse(storage);
            if (savedDarkMode === true) {
                document.getElementsByTagName("html")[0].classList.add("theme-dark");
            }
        }
    </script>
    <!-- MathJax configuration -->
    <script type="text/x-mathjax-config">
        const backgroundBoxShadow = {
            "box-shadow": "5px 5px 15px var(--background-color)",
            "-webkit-box-shadow": "5px 5px 15px var(--background-color)",
            "-moz-box-shadow": "5px 5px 15px var(--background-color)",
            "-khtml-box-shadow": "5px 5px 15px var(--background-color)",
            "filter": "none",
        };
        const sizeS1BorderRadius = {
            "border-radius": "var(--size-s1)",
            "-webkit-border-radius": "var(--size-s1)",
            "-moz-border-radius": "var(--size-s1)",
            "-khtml-border-radius": "var(--size-s1)",
        };
        const popupStyles = {
            "background-color": "var(--background-color) !important",
            "color": "var(--default-font-color) !important",
            "border": "2px solid var(--border-color) !important",
            "box-shadow": "none !important",
            "-webkit-box-shadow": "none !important",
            "-moz-box-shadow": "none !important",
            "-khtml-box-shadow": "none !important",
            "filter": "none !important",
            "border-radius": "var(--size-s2) !important",
            "-webkit-border-radius": "var(--size-s2) !important",
            "-moz-border-radius": "var(--size-s2) !important",
            "-khtml-border-radius": "var(--size-s2) !important",
        };
        MathJax.Hub.Config({
            menuSettings: {
                zoom: "Double-Click",
                zscale: "300%"
            },
            styles: {
                "#MathJax_Zoom": {
                    "background-color": "var(--background-color)",
                    "border": "1px solid var(--border-color)",
                    ...backgroundBoxShadow,
                },
                ".MathJax_Menu": {
                    "background-color": "color-mix(in oklab, var(--background-color) 96%, var(--default-gray))",
                    "color": "var(--default-font-color)",
                    "border": "1px solid var(--border-color)",
                    ...sizeS1BorderRadius,
                    ...backgroundBoxShadow,
                },
                ".MathJax_MenuActive": {
                    "background-color": "var(--toc-hover-color)",
                    "color": "inherit"
                },
                ".MathJax_MenuDisabled:focus, .MathJax_MenuLabel:focus": {
                    "background-color": "var(--toc-hover-color)",
                },
                "#MathJax_Help": popupStyles,
                "#MathJax_About": popupStyles,
                "#MathJax_About > span": {
                    "background-color": "var(--code-background) !important",
                },
                "#MathJax_HelpContent": {
                    "background-color": "var(--code-background) !important",
                },
                ".MathJax_MenuClose": {
                    "border": "none"
                },
                ".MathJax_MenuClose span": {
                    "background-color": "none",
                }
            }
        });
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
                <div class="dropdown theme-dark_mobile" data-role="dropdown" id="toc-dropdown">
                    <ul role="listbox" id="toc-listbox" class="dropdown--list dropdown--list_toc-list"
                        data-role="dropdown-listbox">
                        <div class="dropdown--header">
                        <span>
                            <@template_cmd name="projectName">
                                ${projectName}
                            </@template_cmd>
                        </span>
                            <button class="button" data-role="dropdown-toggle" aria-label="Close table of contents">
                                <i class="ui-kit-icon ui-kit-icon_cross"></i>
                            </button>
                        </div>
                        <div class="sidebar--inner" id="sideMenu"></div>
                    </ul>
                    <div class="dropdown--overlay"></div>
                </div>
            </div>
            <div id="main">
                <@content/>
                <@footer.display/>
            </div>
        </div>
    </div>
</body>
</html>
