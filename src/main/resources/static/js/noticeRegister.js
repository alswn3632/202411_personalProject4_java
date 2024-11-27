var noticeBox = new FroalaEditor('#noticeBox', {
  // 한글 패치
  language: 'ko',
  // Set the save URL.
  saveURL: '/notice/register',
  // HTTP request type.
  saveMethod: 'POST',

  // 이미지 업로드 처리
  imageUploadURL: '/notice/upload',
  imageUploadMethod : 'POST',
  imageUploadParam: 'file'
})

document.getElementById("regBtn").addEventListener('click', ()=>{
  const title = document.getElementById('title')
  const writer = document.getElementById('writer')
  const userId = document.getElementById('userId')

  let formData = {
    content : noticeBox.el.innerHTML,
    title : title.value,
    writer : writer.value,
    userId : parseInt(userId.value),
  }

  saveNotice(formData).then(result =>{
    if(result == "ok"){
      console.log("굿!");
    }else{
      console.log("놉!");
    }
  })
})

async function saveNotice(formData) {
    console.log("저장을 시작합니다.");

    const url = "/notice/register";
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


