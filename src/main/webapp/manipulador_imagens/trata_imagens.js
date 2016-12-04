function contrastImage(imageData, contrast) {  // contrast input as percent; range [-1..1]
    var data = imageData.data;  // Note: original dataset modified directly!
    contrast *= 255;
    var factor = (contrast + 255) / (255.01 - contrast);  //add .1 to avoid /0 error.

    for(var i=0;i<data.length;i+=4)
    {
        data[i] = factor * (data[i] - 128) + 128;
        data[i+1] = factor * (data[i+1] - 128) + 128;
        data[i+2] = factor * (data[i+2] - 128) + 128;
    }
    return imageData;  //optional (e.g. for filter function chaining)
};

function brilho(pixels, brl) {
    var dA = pixels.data;
    var d = pixels.data;
    var brightnessMul = brl; // brightness multiplier
    for(var i = 0; i < dA.length; i += 4){
        var red = dA[i]; // Extract original red color [0 to 255]
        var green = dA[i + 1]; // Extract green
        var blue = dA[i + 2]; // Extract blue

        brightenedRed = brightnessMul * red;
        brightenedGreen = brightnessMul * green;
        brightenedBlue = brightnessMul * blue;
        dA[i] = brightenedRed;
        dA[i + 1] = brightenedGreen;
        dA[i + 2] = brightenedBlue;
    }
    return pixels;
};


function saturate(pixels,vl) {
    var dA = pixels.data; // raw pixel data in array

    var sv = vl; // saturation value. 0 = grayscale, 1 = original

    var luR = 0.3086; // constant to determine luminance of red. Similarly, for green and blue
    var luG = 0.6094;
    var luB = 0.0820;

    var az = (1 - sv)*luR + sv;
    var bz = (1 - sv)*luG;
    var cz = (1 - sv)*luB;
    var dz = (1 - sv)*luR;
    var ez = (1 - sv)*luG + sv;
    var fz = (1 - sv)*luB;
    var gz = (1 - sv)*luR;
    var hz = (1 - sv)*luG;
    var iz = (1 - sv)*luB + sv;

    for(var i = 0; i < dA.length; i += 4){
        var red = dA[i]; // Extract original red color [0 to 255]. Similarly for green and blue below
        var green = dA[i + 1];
        var blue = dA[i + 2];

        var saturatedRed = (az*red + bz*green + cz*blue);
        var saturatedGreen = (dz*red + ez*green + fz*blue);
        var saturateddBlue = (gz*red + hz*green + iz*blue);

        dA[i] = saturatedRed;
        dA[i + 1] = saturatedGreen;
        dA[i + 2] = saturateddBlue;
    }
    return pixels;
};

function bw(pixels,threshold){
    var d = pixels.data;
    for (var i=0; i<d.length; i+=4) {
      var r = d[i];
      var g = d[i+1];
      var b = d[i+2];
      var v = (0.2126*r + 0.7152*g + 0.0722*b >= threshold) ? 255 : 0;
      d[i] = d[i+1] = d[i+2] = v
    }
    return pixels;
}

function  postarParaOcr(txt){
    $.ajax({
        type: "POST",
        url:  "ocr",
        data: {"img_str":txt},
        success: function (data) {    
            $('#modal_carregando').modal('hide');
            //document.getElementById("corpo_upload").innerHTML = data;
            alert(data);
            
        }, 
        error: function (jXHR, textStatus, errorThrown) {
            $('#modal_carregando').modal('hide');
            alert("ops");  
        }
    });
}

function redimensionaTrataEEnviaImagem(width) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function(event) {
            var img = new Image();
            img.onload = function() {
                var oc = document.createElement('canvas'), octx = oc.getContext('2d');
                oc.width = img.width;
                oc.height = img.height;
                octx.drawImage(img, 0, 0);
                while (oc.width * 0.5 > width) {
                  oc.width *= 0.5;
                  oc.height *= 0.5;
                  octx.drawImage(oc, 0, 0, oc.width, oc.height);
                }
                oc.width = width;
                oc.height = oc.width * img.height / img.width;
                octx.drawImage(img, 0, 0, oc.width, oc.height);
                var origBits = octx.getImageData(0, 0, oc.width, oc.height);
                //saturate(origBits,0);
                //brilho(origBits,1.10 );
                bw(origBits, 210);
                //contrastImage(origBits,.8);
                octx.putImageData(origBits, 0, 0);
                document.getElementById('great-image').src = oc.toDataURL();
                postarParaOcr(oc.toDataURL());
            };
            document.getElementById('original-image').src = event.target.result;
            img.src = event.target.result;
        };
        reader.readAsDataURL(input.files[0]);
    }
}