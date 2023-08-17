let page = 0; // Initialize the page number

function loadMoreArticles() {
    $.ajax({
        url: `/ootd/articles?cursor=${cursor}`,
        method: "GET",
        success: function(data) {
            if (data.length > 0) {
                // Append new articles to the container
                const articlesContainer = $("#articles-container");
                for (const article of data) {
                    const articleElement = createArticleElement(article);
                    articlesContainer.append(articleElement);
                }
                cursor = data[data.length - 1].id; // Update the cursor
            }
        },
        error: function() {
            console.error("Error loading articles.");
        }
    });

}

function createArticleElement(article) {
    // Create and return an HTML element for the article
    // You can customize the rendering of each article here
    const articleElement = $("<div>").addClass("article");
    articleElement.append($("<h2>").text(article.title));
    articleElement.append($("<p>").text(article.content));
    return articleElement;
}

// Load more articles when user scrolls near the bottom
$(window).scroll(function() {
    if ($(window).scrollTop() + $(window).height() >= $(document).height() - 100) {
        loadMoreArticles();
    }
});

// Initial loading of articles
loadMoreArticles();