function onLoadAssignments() {
    var node = document.getElementById('root');
    var all = node.getElementsByTagName('g');

    for (var i = -1, l = all.length; ++i < l; ) {
        if(all[i].getAttribute("id") != "null") {
            all[i].setAttribute("fill", "white");

            all[i].onclick = function (evt) {
                var targetshape = evt.target.parentNode;
                var alltargetshapechildren = targetshape.getElementsByTagName('g');
                var idArray = new Array();
                for (var a = -1, b = alltargetshapechildren.length; ++a < b; ) {
                    alltargetshapechildren[a].setAttribute("fill", "yellow");
                    idArray[a] = alltargetshapechildren[a].id;
                }
                targetshape.setAttribute("fill", "yellow");
                idArray[alltargetshapechildren.length] = targetshape.id;
                window.android.clickAndroid(idArray);
                return false;
            };

            all[i].onmouseout = function (evt) {
                var targetshape = evt.target.parentNode;
                var alltargetshapechildren = targetshape.getElementsByTagName('g');
                targetshape.setAttribute("fill", "white");
                for (var a = -1, b = alltargetshapechildren.length; ++a < b; ) {
                    alltargetshapechildren[a].setAttribute("fill", "white");
                }
                window.android.mouseOutAndroid();
                return false;
            };
        }
    }
}