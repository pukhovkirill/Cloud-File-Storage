function copyLink(){
    sendShareRequest(
        function(result){
            console.log(result);
            navigator.clipboard.writeText(result).then(function() {
                console.log('Async: Copying to clipboard was successful!');
                alert('Copying to clipboard');
            });
        }
    )
}

function ready(){
    sendShareRequest(() => {})
}

function sendShareRequest(successFunc){
    const data = {
        path: document.getElementById('select-item').value,
        access: JSON.parse(document.getElementById('access-level').value)
    };

    if(data.path === 'Open this select menu'){
        return;
    }

    $.ajax({
        type: "POST",
        url: '/share-item',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: successFunc,
        error: function (e){
            console.log("error");
            console.log(e);
        }
    });
}