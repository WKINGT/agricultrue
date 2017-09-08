(function($) {
    FormValidation.Validator.cnZipCode = {
        /**
         * @param {FormValidation.Base} validator The validator plugin instance
         * @param {jQuery} $field The jQuery object represents the field element
         * @param {Object} options The validator options
         * @returns {Boolean}
         */
        validate: function(validator, $field, options) {
        	options.country = 'CN';
            return FormValidation.Validator.zipCode.validate(validator, $field, options);
        }
    };
}(window.jQuery));
(function($) {
	var idcode = idcode || {};
	idcode = {
	    chksex : function(idc){
	        var idclen = idc.length ;
	        if( 0 != idc.substr(idclen-2,1)%2){
	            return 1;
	        }else{
	            return 0;
	        }
	        return 0;
	    },
	    chkbirthday : function(idc){
	        var idclen = idc.length ;
	        if(15 == idclen){
	            year = idc.substr(6,2);
	            month = idc.substr(8,2);
	            day  = idc.substr(10,2);
	            return "19"+year+"-"+month+"-"+day;
	        }else if(18 == idclen){
	            year = idc.substr(6,4);
	            month = idc.substr(10,2);
	            day = idc.substr(12,2);
	            return year+"-"+month+"-"+day;
	        }
	    },
	    idcard_verify_number:function(idcard_base){// 计算身份证校验码，根据国家标准GB 11643-1999 
	        if (idcard_base.length != 17)	 return false; 
		// 加权因子 
	        factor = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2]; 
	        // 校验码对应值 
	        verify_number_list = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2']; 
	        checksum = 0; 
	        for (var i = 0; i < idcard_base.length; i++)
	        { 
	            checksum += idcard_base.substr(i,1)*factor[i];
	        } 
	        mod = checksum % 11; 
	        verify_number = verify_number_list[mod]; 
	        return verify_number; 
	    },
	    IDCheck:function(idcard){
	        if (idcard.length  == 15)
			idcard=this.idcard_15to18(idcard);
			if (idcard.length !=18) return false; 
			idcard_base = idcard.substr( 0, 17); 
			if (this.idcard_verify_number(idcard_base) != idcard.substr(17, 1).toLowerCase())
			{ 
				return false; 
			}else{ 
				return true; 
			} 
	    },
	    idcard_15to18:function(idcard){// 将15位身份证升级到18位 
	        if (idcard.length  != 15)return false;
	        // 如果身份证顺序码是996 997 998 999，这些是为百岁以上老人的特殊编码 
	        ids = ['996', '997', '998', '999'];
	        temp_ = idcard.substr(12,3);
	        flag = false;
	        for(var key in ids){
	            if(ids[key] == temp_){
	                flag = true;
	                break;
	            }
	        }
	        if(flag){
	            idcard = idcard.substr(0,6)+"18"+idcard.substr(6,9);
	        }else{
	            idcard = idcard.substr(0,6)+"19"+idcard.substr(6,9);
	        }
	        idcard = idcard + this.idcard_verify_number(idcard); 
	        return idcard; 
	    },
	    idcard_checksum18:function(idcard){// 18位身份证校验码有效性检查 
	        if (idcard.length != 18) return false;
	        idcard_base = idcard.substr( 0, 17); 
	        if (this.idcard_verify_number(idcard_base) != idcard.substr(17, 1).toLowerCase()){ 
	                return false; 
	        }
	        else{ 
	                return true; 
	        } 
	    }
	}
	
FormValidation.Validator.cnId = {
        /**
         * @param {FormValidation.Base} validator The validator plugin instance
         * @param {jQuery} $field The jQuery object represents the field element
         * @param {Object} options The validator options
         * @returns {Boolean}
         */
        validate: function(validator, $field, options) {
        	return  {
        		valid: idcode.IDCheck($field.val()),
                message: '请输入正确的身份证号码'
        	}
			//options.country = 'CN';
            //return FormValidation.Validator.id.validate(validator, $field, options);
        }
    };
}(window.jQuery));
(function($) {
FormValidation.Validator.cnPhone = {
        /**
         * @param {FormValidation.Base} validator The validator plugin instance
         * @param {jQuery} $field The jQuery object represents the field element
         * @param {Object} options The validator options
         * @returns {Boolean}
         */
        validate: function(validator, $field, options) {
			options.country = 'CN';
            return FormValidation.Validator.phone.validate(validator, $field, options);
        }
    };
}(window.jQuery));
(function($) {
FormValidation.Validator.uploaderNotEmpay = {
		
        /**
         * @param {FormValidation.Base} validator The validator plugin instance
         * @param {jQuery} $field The jQuery object represents the field element
         * @param {Object} options The validator options
         * @returns {Boolean}
         */
        validate: function(validator, $field, options) {
            var valid =  FormValidation.Validator.notEmpty.validate(validator, $field, options);
            var msg = '文件必须上传';
            if(valid && typeof options['min'] != undefined){
            	var d = $field.val();
            	if(d.split(",").length <options.min){
            		valid = false;
            		msg = '至少上传'+options.min+'个文件';
            	}
            }
            var c = $field.parent().find('.xgs-uploader');
            if(valid){
            	c.css({'border-color':'#7ba065'});
            }else{
            	c.css({'border-color':'#d16e6c'});
            }
            return {
            	valid:valid,
            	message:msg
            }
        }
    };
}(window.jQuery));