'use strict';
(function ($){
    let methods = {
        init: function (json, settings) {
            this.json = json;
            this.settings = $.extend({
                tryAplyMaskMoney: true,
                triggerName: 'mask.maskMoney',
                undescoredToCamelCase: true,
                checkUndefined:true,
                overrideValues:false
            }, settings);
            return true;
        },

        setElementValue: function (element) {
            let keyJson = element.attr('id');
            if (this.settings.undescoredToCamelCase) {
                keyJson = methods.idElementToKeyJson(keyJson);
            }
            let val = this.json[keyJson];
            methods.setElement(element,val);
            return true;
        },

        setElement: function (element, val) {
            if (val !== undefined || !this.settings.checkUndefined) {
                if (methods.elementContainsValue(element)) {
                    element.val(val);
                    if (this.settings.tryAplyMaskMoney) {
                        try {
                            element.trigger(this.settings.triggerName);
                        } catch (e) {
                            console.log(e);
                        }
                    }
                }
            }
        },

        elementContainsValue: function (element) {
            return !element.val() || this.settings.overrideValues;
        },

        setFromInputsValues: function (form) {
            $.each($('input, select ,textarea', form),function(k){
                console.log(k+' '+$(this).attr('name'));
                methods.setElementValue($(this));
            });
            return true;
        },

        setFromJsonValues: function () {
            $.each(this.json, function(k, v) {
                console.log(k + ' is ' + v);
                let element = $('#'+methods.keyJsonToIdElement(k));
                methods.setElement(element,v);
            });
            return true;
        },

        keyJsonToIdElement: function (keyJson) {
            for(let i=0;i<keyJson.length;i++){
                let part = keyJson.substring(i, (i + 1));
                if(part===part.toUpperCase()){
                    let ini;
                    if(i>0){
                        ini = keyJson.substring(0, i);
                    }else {
                        ini = "";
                    }
                    let end;
                    if((i+1)<keyJson.length) {
                        end = keyJson.substring((i + 1), keyJson.length);
                    }else {
                        end ="";
                    }
                    part = "_"+part.toLowerCase();
                    keyJson = ini + part + end;
                    i++;
                }
            }
            return keyJson;
        },

        idElementToKeyJson: function (idElement) {
            while (idElement.indexOf("_") >= 0) {
                let idx = idElement.indexOf("_");
                idElement = idElement.replace("_", "");
                let part = idElement.substring(idx, (idx + 1));
                part = part.toUpperCase();
                let ini = idElement.substring(0, idx);
                let end = idElement.substring((idx + 1), idElement.length);
                idElement = ini + part + end;
            }
            return idElement;
        }
    };

    $.fn.setInput = function (json, settings){
        if(typeof settings === "object" ){
             methods.init(json,settings);
        }else {
             methods.init(json);
        }
        methods.setElementValue($(this));
    };

    $.fn.setForm = function (json, settings) {
        if(typeof settings === "object" ){
            methods.init(json,settings);
        }else {
            methods.init(json);
        }
        return methods.setFromInputsValues($(this));
    };

    $.fn.setFromJson = function (json, settings) {
        if(typeof settings === "object" ){
            methods.init(json,settings);
        }else {
            methods.init(json);
        }
        return methods.setFromJsonValues($(this));
    }
})(window.jQuery || window.Zepto);