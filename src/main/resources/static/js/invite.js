$('#invite-btn').on("click", function () {
    let challengeId = $("#challenge-id").val();
    let userId = $("#user-id").val();
    console.log(userId);
    inviteUser(userId, challengeId);
});

function inviteUser(userId, challengeId) {
    $.ajax({
        url: "/api/v1/challenges/"+challengeId+"/invite/"+userId,
        type: "POST",
        data: JSON.stringify(userId),
        contentType: "application/json; charset=utf-8",
    }).done(function(response) {
        console.log(response);

    }).fail(function(request, error) {
        console.log(JSON.stringify(request.responseText));
        alert(JSON.stringify(request.responseText));
    });
}

/*
function updateLike(certificationId) {
    let likeCnt = document.getElementById("like-cnt")
    $.ajax({
        url: "/api/v1/certification/" + certificationId + "/likes",
        type: "POST",
        data: JSON.stringify(certificationId),
        contentType: "application/json; charset=utf-8",
    }).done(function (response) {
        console.log(response)
        if (response.message === "좋아요 생성 성공") {
            $("#likeImg").attr("src", "/assets/like_click.png");
            $("#like-cnt").text($("#like-cnt").val() + 1)
        } else if (response.message === "좋아요 취소") {
            $("#likeImg").attr("src", "/assets/like_empty.png");
            console.log($("#like-cnt").text())
            $("#like-cnt").text($("#like-cnt").text()-1)
        }
    }).fail(function (error) {
        console.log(JSON.stringify(error))
        alert(JSON.stringify(error));
    });
}
 */