const toggleSidebar = () => {
    if ($(".user-sidebar").is(":visible")) {
        $(".user-sidebar").css("display", "none");
        $(".user-content").css("margin-left", "0%");

    } else {
        $(".user-sidebar").css("display", "block");
        $(".user-content").css("margin-left", "20%");

    }
}

document.getElementById('focusOnClick').focus();