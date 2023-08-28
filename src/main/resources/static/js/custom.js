document.addEventListener("DOMContentLoaded", function () {
    const createButton = document.querySelector(".create-button");

    if (createButton) {
        createButton.addEventListener("click", function () {
            window.location.href = "/issueArticles/view/create";
        });
    }
});
