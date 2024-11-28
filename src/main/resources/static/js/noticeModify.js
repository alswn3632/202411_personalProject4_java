
console.log("noticeModify.js in!!");

var noticeBox = new FroalaEditor('#noticeBox', {
    // 한글 패치
    language: 'ko',

    toolbarButtons: {
        'moreText': {
          'buttons': ['bold', 'italic', 'underline', 'strikeThrough', 'subscript', 'superscript', 'fontFamily', 'fontSize', 'textColor', 'backgroundColor', 'inlineClass', 'inlineStyle', 'clearFormatting']
        },
        'moreParagraph': {
          'buttons': ['alignLeft', 'alignCenter', 'formatOLSimple', 'alignRight', 'alignJustify', 'formatOL', 'formatUL', 'paragraphFormat', 'paragraphStyle', 'lineHeight', 'outdent', 'indent', 'quote']
        },
        'moreRich': {
          'buttons': ['insertLink', 'insertImage', 'emoticons', 'insertTable', 'fontAwesome', 'specialCharacters', 'embedly', 'insertHR']
        }
    },

    // 이미지 업로드 처리
    imageUploadURL: '/notice/upload',
    imageUploadMethod: 'POST',
    imageUploadParam: 'file',

    events: {
        // 이미지 삽입 시 실행되는 함수
        'image.inserted': function (image) {
            console.log('이미지 삽입됨:', image[0].currentSrc);
        },

        // 이미지 삭제 시 실행되는 함수
        'image.removed': function (image) {
            console.log("이미지 삭제 진행 : " + image[0].currentSrc);
            let url = image[0].currentSrc
            let uuid = url.match(/\/([^\/_]+)_[^\/]+$/);
            deleteTempFile(uuid[1]).then(result => {
                if (result == "1") {
                } else {
                    console.log("이미지 삭제 작업을 진행 중 오류가 발생했습니다. ");
                }
            });
        }
    }
})

document.getElementById("regBtn").addEventListener('click', () => {
    const title = document.getElementById('title')
    const writer = document.getElementById('writer')
    const userId = document.getElementById('userId')
    const noticeId = document.getElementById('noticeId')

    let formData = {
        content: noticeBox.el.innerHTML,
        title: title.value,
        writer: writer.value,
        userId: parseInt(userId.value),
        noticeId: parseInt(noticeId.value)
    }

    saveNotice(formData).then(result => {
        if (result == "1") {
            location.href = "/notice/list";
        } else {
            alert("삭제 작업 중 오류가 발생했습니다.")
        }
    })
})

async function saveNotice(formData) {
    console.log("저장을 시작합니다.");

    const url = "/notice/modify";
    const config = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        body: JSON.stringify(formData)
    };

    const resp = await fetch(url, config);
    const result = await resp.text();
    return result;
}

async function deleteTempFile(uuid) {
    const url = "/notice/deleteFile/" + uuid;
    const resp = await fetch(url);
    const result = await resp.text();
    return result;
}