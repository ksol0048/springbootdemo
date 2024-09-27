async function uploadToServer(formObj){
    console.log("upload to server...");
    console.log(formObj);

    const response = await axios({
        method: 'POST',
        url: '/upload',
        data: formObj,
        header:{
            'Content-Type': 'multipart/form-data'
        }
    })
    console.log(response)
    return response.data
}

async function removeFileToServer(uuid,fileName){
    const response =await axios.delete(`/remove/${uuid}_${fileName}`);

    return response.data;
}