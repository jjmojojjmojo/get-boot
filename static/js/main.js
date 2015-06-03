$(document).ready(function() {
    $.get("info",
      function(data){
        $("#version").html(data.version);
        $("#release-link").html(data['tag-name']);
        $("#release-link").attr("href", data.url);
      }
    );
});
