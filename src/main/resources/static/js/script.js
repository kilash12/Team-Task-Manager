//console.log("Theme Script loaded");
//
//// Get saved theme or check system preference
//let currentTheme = getTheme();
//
//document.addEventListener("DOMContentLoaded", () => {
//    initTheme();
//});
//
//// Initialize Theme
//function initTheme() {
//    applyTheme(currentTheme);
//
//    // Use querySelectorAll in case multiple toggle buttons exist (e.g., mobile/desktop or fragments)
//    const changeThemeButtons = document.querySelectorAll("#theme_change_button");
//    changeThemeButtons.forEach(button => {
//        button.addEventListener("click", () => {
//            currentTheme = currentTheme === "dark" ? "light" : "dark";
//            applyTheme(currentTheme);
//        });
//    });
//}
//
//// Apply Theme to Page and Update Button UI
//function applyTheme(theme) {
//    const htmlTag = document.documentElement;
//
//    // Reset and apply theme classes
//    htmlTag.classList.remove("light", "dark");
//    htmlTag.classList.add(theme);
//
//    // Save to localStorage
//    localStorage.setItem("theme", theme);
//
//    // Update all theme buttons on the page
//    const themeButtons = document.querySelectorAll("#theme_change_button");
//    themeButtons.forEach(btn => {
//        const span = btn.querySelector("span");
//        const icon = btn.querySelector("i");
//
//        if (span) {
//            span.textContent = theme === "light" ? " Dark Mode" : " Light Mode";
//        }
//
//        if (icon) {
//            // Consistent icons for the toggle
//            // If theme is light, show moon (suggesting switch to dark)
//            // If theme is dark, show sun (suggesting switch to light)
//            icon.className = theme === "light" ? "fa-solid fa-moon text-lg" : "fa-solid fa-sun text-lg";
//        }
//    });
//}
//
//// Get Theme Function
//function getTheme() {
//    const savedTheme = localStorage.getItem("theme");
//    if (savedTheme) {
//        return savedTheme;
//    }
//    // Default to system preference if no saved theme
//    return window.matchMedia('(prefers-color-scheme: dark)').matches ? "dark" : "light";
//}


let currentTheme = getTheme();

document.addEventListener("DOMContentLoaded", () => {
    applyTheme(currentTheme);

    document.querySelectorAll(".theme_change_button").forEach(btn => {
        btn.addEventListener("click", () => {
            currentTheme = currentTheme === "dark" ? "light" : "dark";
            applyTheme(currentTheme);
        });
    });
});

function applyTheme(theme) {
    const html = document.documentElement;
    html.classList.remove("light", "dark");
    html.classList.add(theme);
    localStorage.setItem("theme", theme);
}

function getTheme() {
    const saved = localStorage.getItem("theme");
    if (saved) return saved;
    return window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light";
}