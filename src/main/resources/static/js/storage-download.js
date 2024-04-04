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

function b64DecodeUnicode(str) {
    return decodeURIComponent(atob(str).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
}

function download(){
    let data = [];
    for (const value of files.values()) {
        data.push(value);
    }
    for (const key of files.keys()){
        const row = document.getElementById(key);
        row.classList.remove('table-active');
    }

    $.ajax({
        type: "POST",
        url: '/download',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (result){
            let bytes = b64DecodeUnicode(result.bytes);
            let type = result.contentType;
            let name = result.name;
            let blob = new Blob([bytes], { type: type });
            let url = window.URL.createObjectURL(blob);
            let a = document.createElement("a");
            a.href = url;
            a.download = name;
            a.click();
            window.URL.revokeObjectURL(url);
        }
    });
    files.clear();
}