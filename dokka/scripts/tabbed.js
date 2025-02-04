/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file tabbed.js is part of kotlin-fuzzy
 * Last modified on 30-09-2023 08:41 p.m.
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
 * KT-FUZZY IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


/**
 * Link all tabs with same content
 */
function tabSync() {
    const tabs = document.querySelectorAll(".tabbed-set > input")
    for (const tab of tabs) {
        tab.addEventListener("click", () => {
            saveTabSelection(tab)

            const current = document.querySelector(`label[for=${tab.id}]`)
            const pos = current.getBoundingClientRect().top

            syncTabsByName(current)

            // Preserve scroll position
            const delta = (current.getBoundingClientRect().top) - pos
            window.scrollBy(0, delta)
        })
    }
}

/**
 * Load previously selected tabs
 */
function loadSavedTabs() {
    let allTabs = document.querySelectorAll(".tabbed-set > input");
    let tabGroups = Array.from(allTabs).map((element) => {
        return element.getAttribute("name")
    })
    tabGroups = unique(tabGroups);

    for (const tabGroup of tabGroups) {
        let tabId = loadTabIdsFromStorage(tabGroup);

        if (tabId == null)
            continue;

        let tab = document.querySelector(`input[id="${tabId}"][name="${tabGroup}"]`)
        tab.checked = true

        const label = document.querySelector(`label[for=${tab.id}]`)
        syncTabsByName(label)
    }
}

/**
 * @param {Element} localLabel
 */
function syncTabsByName(localLabel) {
    const labelContent = localLabel.innerHTML
    const labels = document.querySelectorAll('.tabbed-set > label, .tabbed-alternate > .tabbed-labels > label')
    for (const label of labels) {
        if (label.innerHTML === labelContent) {
            let input = document.querySelector(`input[id=${label.getAttribute('for')}]`);
            input.checked = true
            if (label !== localLabel)
                clearTabSelection(input)
        }
    }
}

/**
 * @returns {boolean}
 */
function isLocalStorageEnabled() {
    try {
        const key = `__local_storage_test`;
        window.localStorage.setItem(key, null);
        window.localStorage.removeItem(key);
        return true;
    } catch (e) {
        return false;
    }
}

let localStorageEnabled = isLocalStorageEnabled();

/**
 * @param {Element} tab
 */
function saveTabSelection(tab) {
    if (!localStorageEnabled)
        return;
    const tabGroupId = tab.getAttribute("name")
    const tabId = tab.id
    localStorage.setItem(`${location.pathname}.${tabGroupId}`, tabId)
}

/**
 * @param {Element} tab
 */
function clearTabSelection(tab) {
    if (!localStorageEnabled)
        return;
    const tabGroupId = tab.getAttribute("name")
    localStorage.removeItem(`${location.pathname}.${tabGroupId}`)
}

/**
 * @param {string} tabGroupId
 * @returns {?string}
 */
function loadTabIdsFromStorage(tabGroupId) {
    if (!localStorageEnabled)
        return null;
    return localStorage.getItem(`${location.pathname}.${tabGroupId}`)
}

/**
 * @template T
 * @param {T[]} array
 * @returns {T[]}
 */
function unique(array) {
    return Array.from(new Set(array));
}

window.addEventListener('DOMContentLoaded', () => {
    // Load shit
    loadSavedTabs()
    tabSync()
})
