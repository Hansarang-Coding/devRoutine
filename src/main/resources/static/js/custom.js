$('#comment-btn').on("click", function () {
    let certificationId = $("#cert-id").val();
    createComment(certificationId);
});
/*
$('#comment-upt-btn').on("click", function () {
    let certificationId = $("#cert-id2").val();
    let commentId = $("#comment-id2").val();
    updateComment(certificationId,commentId);
});*/

$("button[name=comment-upt-btn]").click(function(){
    var index=$(this).attr("id");
    let certificationId=$("#cert-id2").val();
    let commentId=$('#comment-id2'+index).val();
    updateComment(index, certificationId, commentId);
});

/*$('#comment-del-btn').on("click", function () {
    let certificationId = $("#cert-id").val();
    let commentId = $("#comment-id").val();
    deleteComment(certificationId,commentId);
});*/

$("button[name=comment-del-btn]").click(function (){
    var index=$(this).attr("id");
    let certificationId=$("#cert-id").val();
    let commentId=$('#comment-id'+index).val();
    deleteComment(certificationId,commentId);
});

$('#like-img').on("click", function () {
    let certificationId = $("#cert-id").val();
    updateLike(certificationId);
});

function createComment(certificationId) {
    let commentDto = {
        comment: $("#commentContent").val()
    };
    $.ajax({
        url: "/api/v1/certification/" + certificationId + "/comments",
        type: "POST",
        data: JSON.stringify(commentDto),
        contentType: "application/json; charset=utf-8",
        dataType: "json"

    }).done(function (response) {
        alert("댓글 작성을 완료하였습니다.")
        location.href = "/certification/" + certificationId
    }).fail(function (error) {
        alert(JSON.stringify(error));
    });
}

function updateComment(index, certificationId, commentId) {
    let commentDto = {
        comment: $('#comment-content-upt'+index).val()
    };
    $.ajax({
        url: "/api/v1/certification/" + certificationId + "/comments/" + commentId,
        type: "PUT",
        data: JSON.stringify(commentDto),
        contentType: "application/json; charset=utf-8",
        dataType: "json"

    }).done(function (response) {
        alert("댓글 수정을 완료하였습니다.")
        location.href = "/certification/" + certificationId
    }).fail(function (error) {
        alert(JSON.stringify(error));
    });
}

function deleteComment(certificationId, commentId) {
    $.ajax({
        url: "/api/v1/certification/" + certificationId + "/comments/" + commentId,
        type: "DELETE"
    }).done(function (response) {
        alert("작성한 댓글이 삭제되었습니다.")
        location.href = "/certification/" + certificationId
    }).fail(function (error) {
        alert(JSON.stringify(error));
    });
}


function updateLike(certificationId) {
    $.ajax({
        url: "/api/v1/certification/" + certificationId + "/likes",
        type: "POST",
        data: JSON.stringify(certificationId),
        contentType: "application/json; charset=utf-8",
        dataType: "json"

    }).done(function (response) {
        console.log(response)
        if (response.message === "좋아요 생성 성공") {
            $("#like-img").attr("src", "/assets/like_click.png");
            $("#like-cnt").text(parseInt($("#like-cnt").text()) + 1)
        } else if (response.message === "좋아요 취소") {
            $("#like-img").attr("src", "/assets/like_empty.png");
            console.log($("#like-cnt").text())
            $("#like-cnt").text($("#like-cnt").text() - 1)
        }
    }).fail(function (error) {
        console.log(JSON.stringify(error))
        alert(JSON.stringify(error));
    });
}

let img = $(".cert-card");
executeImageModal(img);

function closeModalByClickOtherTab() {
    $(".modal").click(function (e) {
        if (e.target.className != "modal") {
            return false;
        } else {
            $(".modal").hide();
        }
    });
}

function closeModalByButton() {
    $(".modal button").click(function () {
        $(".modal").hide();
    });
}

function executeImageModal() {
    $(function () {
        $(img).click(function () {
            $(".modal").show();
            // 해당 이미지 가겨오기
            let imgSrc = $(this).children("img").attr("src");
            let imgAlt = $(this).children("img").attr("alt");
            $(".modalBox img").attr("src", imgSrc);
            $(".modalBox img").attr("alt", imgAlt);

            // 해당 이미지 텍스트 가져오기
            let imgTit = $(this).children("p").text();
            $(".modalBox p").text(imgTit);

            // 해당 이미지에 alt값을 가져와 제목으로
            //$(".modalBox p").text(imgAlt);
        });

        //.modal안에 button을 클릭하면 .modal닫기
        closeModalByButton();
        //.modal밖에 클릭시 닫힘
        closeModalByClickOtherTab();
    });
}