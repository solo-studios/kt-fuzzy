/*
 * Copyright (c) 2025 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file modify-browsers.js is part of kotlin-fuzzy
 * Last modified on 23-09-2025 12:17 a.m.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * KOTLIN-FUZZY IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


// noinspection JSUnnecessarySemicolon
;(function (config) {
    /** @type {{[key: string]: Object}} */
    const customLaunchers = {};

    for (/** @type {string} */ let browser of config.browsers) {
        const customBrowser = {base: browser};

        const lowerBrowser = browser.toLowerCase();
        if (lowerBrowser.includes("chrome") || lowerBrowser.includes("chromium")) {
            customBrowser.flags = [
                "--disable-client-side-phishing-detection",
                "--disable-features=Translate",
                "--disable-notifications",
                "--mute-audio",
                "--password-store=basic",
                "--use-mock-keychain",
            ];
        } else if (lowerBrowser.includes("firefox")) {
            // TODO 2025-09-22 (solonovamax): any flags that I should set?
        }

        customLaunchers[`${browser}_custom`] = customBrowser;
    }

    config.set({customLaunchers: customLaunchers});

    config.browsers = config.browsers.map(browser => `${browser}_custom`);
})(config);
