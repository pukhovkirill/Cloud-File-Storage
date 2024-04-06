const files = new Map();

function addToChosen(e){
    let index = e.id;
    let path = document.getElementById('path-'+index);
    let isActive = e.classList.contains('table-active');
    if(isActive){
        e.classList.remove('table-active');
        if(files.has(index))
            files.delete(index);
    }else{
        e.classList.add('table-active');
        files.set(index, path.innerText);
    }
}

function clearFilesMap(){
    files.clear();
}

function download(){
    if(files.size === 0)
        return;

    let data = [];
    for (const value of files.values()) {
        data.push(value);
    }
    for (const key of files.keys()){
        const row = document.getElementById(key);
        row.classList.remove('table-active');
    }

    let nameLen = 0;
    let offset = 4;

    function readName(buff){
        const int8A = new Int8Array(buff.slice(0, 4));
        nameLen =
             int8A[0]        |
            (int8A[1] << 8)  |
            (int8A[1] << 16) |
            (int8A[1] << 24);

        const decoder = new TextDecoder();
        return decoder.decode(buff.slice(offset, offset + nameLen));
    }

    function prepare(buff){
        return buff.slice(offset+nameLen, buff.length);
    }

    $.ajax({
        type: "POST",
        url: '/download',
        contentType: 'application/json',
        data: JSON.stringify(data),
        xhrFields:{
            responseType: 'blob'
        },
        success: async function (result) {
            const buff = await result.arrayBuffer();

            let name = readName(buff);
            let bytes = prepare(result);

            let blob = new Blob([bytes], {type: "application/octet-stream"});
            let url = window.URL.createObjectURL(blob);
            let a = document.createElement("a");

            a.href = url;
            a.download = name;
            a.click();
            window.URL.revokeObjectURL(url);
        },
        error: function (e){
            console.log("error");
            console.log(e);
        }
    });
    clearFilesMap();
}