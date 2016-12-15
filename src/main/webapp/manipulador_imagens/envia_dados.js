
var dropZone = document.getElementById('corpo_upload');

// Optional.   Show the copy icon when dragging over.  Seems to only work for chrome.
dropZone.addEventListener('dragover', function(e) {
    e.stopPropagation();
    e.preventDefault();
    e.dataTransfer.dropEffect = 'copy';
});

// Get file data on drop
dropZone.addEventListener('drop', function(e) {
    $('#modal_carregando').modal('show');
    var tamanho = 2000;
    e.stopPropagation();
    e.preventDefault();
    var files = e.dataTransfer.files; // Array of all files
    for (var i=0, file; file=files[i]; i++) {
        if (file.type.match(/image.*/)) {
            var reader = new FileReader();

            reader.onload = function(event) {
                var img = new Image();
                img.onload = function() {
                    var oc = document.createElement('canvas'), octx = oc.getContext('2d');
                    oc.width = img.width;
                    oc.height = img.height;
                    octx.drawImage(img, 0, 0);
                    while (oc.width * 0.5 > tamanho) {
                      oc.width *= 0.5;
                      oc.height *= 0.5;
                      octx.drawImage(oc, 0, 0, oc.width, oc.height);
                    }
                    oc.width = tamanho;
                    oc.height = oc.width * img.height / img.width;
                    octx.drawImage(img, 0, 0, oc.width, oc.height);
                    var origBits = octx.getImageData(0, 0, oc.width, oc.height);
                    //saturate(origBits,0);
                    //brilho(origBits,1.10 );
                    bw(origBits, 210);
                    //contrastImage(origBits,.8);
                    octx.putImageData(origBits, 0, 0);
                    document.getElementById('original-image').src = oc.toDataURL();
                    $("#div-original-image").css("display","block");
                    postarParaOcr(oc.toDataURL());
                };
                img.src = event.target.result;
            };
            reader.readAsDataURL(file); // start reading the file data.
        }   
    } 
});