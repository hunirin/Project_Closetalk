let cursor = null; // Initialize the cursor

function loadMoreArticles() {
    $.ajax({
        url: `/ootd/list?cursor=${cursor}`,
        method: "GET",
        success: function(data) {
            if (data.ootdPage.content.length > 0) {
                // Append new articles to the container
                const articlesContainer = $("#articles-container");
                for (const ootdPage of data.ootdPage.content) {
                    const articleElement = createArticleElement(ootdPage);
                    articlesContainer.append(articleElement);
                }
                cursor = data.ootdPage.content[data.ootdPage.content.length - 1].id; // Update the cursor
            }
        },
        error: function() {
            console.error("Error loading articles.");
        }
    });
}

function createArticleElement(ootdPage) {
    // Create and return an HTML element for the article
    const articleElement = $("<div>").addClass("ootdPage");
    articleElement.append($("<h2>").text(ootdPage.content));
    articleElement.append($("<p>").text(ootdPage.hashtag));

    // Display the thumbnail image if available
    if (ootdPage.thumbnail) {
        const thumbnailElement = $("<img>").attr("src", ootdPage.thumbnail).attr("alt", "Thumbnail");
        articleElement.append(thumbnailElement);
    }

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