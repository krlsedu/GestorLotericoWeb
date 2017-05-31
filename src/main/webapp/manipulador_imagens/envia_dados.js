
var dropZone = document.getElementById('corpo_upload');

// Optional.   Show the copy icon when dragging over.  Seems to only work for chrome.
dropZone.addEventListener('dragover', function(e) {
    e.stopPropagation();
    e.preventDefault();
    e.dataTransfer.dropEffect = 'copy';
});
var originalVal;
var imgOri  = new Image();
var imgProc  = new Image();
$("#bw").slider();
$("#bw").on("slide", function(slideEvt) {
	$("#bwSliderVal").text(slideEvt.value);
});
$("#bw").slider().on('slideStart', function(ev){
    originalVal = $("#bw").data('slider').getValue();
});
$("#bw").on('slideStop', function(ev){
    var newVal = $("#bw").data('slider').getValue();
    if(originalVal != newVal) {
        altera(1000,$("#bw").data('slider').getValue(),$("#brl").data('slider').getValue());
    }
});

$("#brl").slider();
$("#brl").on("slide", function(slideEvt) {
	$("#brlSliderVal").text(slideEvt.value);
});
$("#brl").slider().on('slideStart', function(ev){
    originalVal = $("#bw").data('slider').getValue();
});
$("#brl").on('slideStop', function(ev){
    var newVal = $("#brl").data('slider').getValue();
    if(originalVal != newVal) {
        altera(1000,$("#bw").data('slider').getValue(),$("#brl").data('slider').getValue());
    }
});

$('#ex20a').on('click', function(e) {
            $('#sld8').toggle()
                .slider('relayout');
            e.preventDefault();
        });

// Get file data on drop
dropZone.addEventListener('drop', function(e) {
    var tamanho = 1000;
    e.stopPropagation();
    e.preventDefault();
    var files = e.dataTransfer.files; // Array of all files
    for (var i=0, file; file=files[i]; i++) {
        if (file.type.match(/image.*/)) {
            var reader = new FileReader();

            reader.onload = function(event) {
                var img = new Image();
                img.onload = function() {
                    imgOri = img;
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
                    if(oc.width>oc.height){
                         
                        var image = new Image();
                        image.src = oc.toDataURL();
                        
                        
                        
                        var canvas = document.createElement('canvas'), ctx = canvas.getContext('2d');
                        canvas.width = image.width;
                        canvas.height = image.width; 
                        ctx.drawImage(image, 0, 0);
                        ctx.clearRect(0,0,canvas.width,canvas.height);
                        ctx.save();
                        ctx.translate(canvas.width/2,canvas.height/2);
                        ctx.rotate(90*Math.PI/180);
                        ctx.drawImage(image,-image.width/2,-image.width/2);
                        document.getElementById('original-image').src = canvas.toDataURL();
                        imgProc.src=canvas.toDataURL();
                        //postarParaOcr(canvas.toDataURL());
                        
                    }else{
                        document.getElementById('original-image').src = oc.toDataURL();
                        imgProc.src=oc.toDataURL();
                        //postarParaOcr(oc.toDataURL());
                    }
                    $("#div-original-image").css("display","block");
                };
                img.src = event.target.result;
            };
            reader.readAsDataURL(file); // start reading the file data.
        }   
    } 
});

function altera(tamanho,pb,brl){
    var img = new Image();
    img = imgOri;
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
    brilho(origBits,brl );
    bw(origBits, pb);
    //contrastImage(origBits,.8);
    octx.putImageData(origBits, 0, 0);
    if(oc.width>oc.height){

        var image = new Image();
        image.src = oc.toDataURL();



        var canvas = document.createElement('canvas'), ctx = canvas.getContext('2d');
        canvas.width = image.width;
        canvas.height = image.width; 
        ctx.drawImage(image, 0, 0);
        ctx.clearRect(0,0,canvas.width,canvas.height);
        ctx.save();
        ctx.translate(canvas.width/2,canvas.height/2);
        ctx.rotate(90*Math.PI/180);
        ctx.drawImage(image,-image.width/2,-image.width/2);
        document.getElementById('original-image').src = canvas.toDataURL();
        imgProc.src=canvas.toDataURL();
        //postarParaOcr(canvas.toDataURL());

    }else{
        document.getElementById('original-image').src = oc.toDataURL();
        imgProc.src=oc.toDataURL();
        //postarParaOcr(oc.toDataURL());
    }
}
$('#enviarImg').on('click', function(e) {
    $('#modal_carregando').modal('show');
    var canvas = document.createElement('canvas'), ctx = canvas.getContext('2d');
    canvas.width = imgProc.width;
    canvas.height = imgProc.width; 
    ctx.drawImage(imgProc, 0, 0);
    postarParaOcr(canvas.toDataURL());
    e.preventDefault();
});


function drawRotated(ctx,canvas,image){
    ctx.clearRect(0,0,canvas.width,canvas.height);
    ctx.save();
    ctx.translate(canvas.width/2,canvas.height/2);
    ctx.rotate(90*Math.PI/180);
    ctx.drawImage(image,-image.width/2,-image.width/2);
    ctx.restore();
}