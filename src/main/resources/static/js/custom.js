function updateComment() {
    let commentBean = {
        name:$("#commentName").val(),
        content: $("#commentContent").val(),
        post_num: $("#commentPostNum").val()
    };
    $.ajax({
        url: "/api/v1/certification/{certificationId}/comments",
        type: "POST",
        data: JSON.stringify(commentBean),
        contentType:"application/json; charset=utf-8", //body 데이터가 어떤 타입인지(MIME)
        dataType: "json"
    }).done(function (fragment) {
            $('#commentTable').replaceWith(fragment);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
}