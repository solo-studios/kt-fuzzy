/*
 * Copyright (c) 2025 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file inject-env.js is part of kotlin-fuzzy
 * Last modified on 23-09-2025 12:18 a.m.
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
    const fs = require("fs");
    const path = require("path");

    /**
     * @param {*} value
     * @return {{[key: string]: *}|boolean|number|string|null}
     */
    function sanitize(value) {
        if (value === undefined || value === null) {
            return null;
        } else if (typeof value === "boolean" || typeof value == "number" || typeof value == "string") {
            return value;
        } else if (Array.isArray(value)) {
            return value.map(item => sanitize(item));
        } else {
            return mapObject(value, nested => sanitize(nested));
        }
    }

    /**
     * @template K
     * @template V
     * @template R
     * @param {{[key: K]: V}} object
     * @param {function(V): R} callback
     * @return {{[key: K]: R}}
     */
    function mapObject(object, callback) {
        return Object.keys(object).reduce((result, key) => {
            result[key] = callback(object[key]);
            return result;
        }, {});
    }

    function envLoaderKarmaPlugin(files) {
        const scriptPath = path.join(config.basePath, "loaded-env.js");
        const scriptContents = `window.env = ${JSON.stringify(sanitize(process.env))}`;

        fs.writeFileSync(scriptPath, scriptContents);

        files.unshift({
            pattern: scriptPath,
            included: true,
            served: true,
            watched: false,
        });
    }

    envLoaderKarmaPlugin.$inject = ["config.files"];

    config.plugins = config.plugins ?? [];
    config.plugins.push({"framework:env-loader": ["factory", envLoaderKarmaPlugin]});

    config.frameworks = config.frameworks ?? [];
    config.frameworks.push("env-loader");
})(config);
