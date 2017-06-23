'use strict';

class MathOps {
    constructor() {
        this.precision = 2;
    }

    static setPrecision(val){
        this.precision = val;
    }

    static inInteger(val) {
        while (val.indexOf(this.thousands)>=0){
            val = val.replace(this.thousands,"");
        }
        if(val.indexOf(this.decimal)>0){
            let decimalPart = val.substring(val.indexOf(this.decimal)+1,val.length);
            val = val.replace(this.decimal,"");
            if(decimalPart.length>this.precision){
                decimalPart = decimalPart.substring(0,this.precision);
            }
            let ret = (parseInt(val)* Math.pow(10,(this.precision-decimalPart.length)));
            return ret;
        }else{
            return (parseInt(val)*Math.pow(10,this.precision));
        }
    }

    static inFloat(val){
        if(val.toString().length>this.precision) {
            let integerPart = val.toString().substring(0, val.toString().length - this.precision);
            let decimalPart = val.toString().substring(val.toString().length - this.precision, val.toString().length);
            return integerPart+'.'+decimalPart;
        }else{
            return '0.'+'0'.repeat(this.precision-val.toString().length)+val;
        }
    }
}
Object.defineProperty(MathOps, 'precision', {
    value: 2,
    writable : true,
    enumerable : true,
    configurable : true
});
Object.defineProperty(MathOps, 'decimal', {
    value: ',',
    writable : true,
    enumerable : true,
    configurable : true
});
Object.defineProperty(MathOps, 'thousands', {
    value: '.',
    writable : true,
    enumerable : true,
    configurable : true
});