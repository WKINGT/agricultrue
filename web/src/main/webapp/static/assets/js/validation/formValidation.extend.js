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
(function($) {FormValidation.Validator.cnId = {
        /**
         * @param {FormValidation.Base} validator The validator plugin instance
         * @param {jQuery} $field The jQuery object represents the field element
         * @param {Object} options The validator options
         * @returns {Boolean}
         */
        validate: function(validator, $field, options) {
			options.country = 'CN';
            return FormValidation.Validator.id.validate(validator, $field, options);
        }
    };
}(window.jQuery));
(function($) {FormValidation.Validator.cnPhone = {
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
(function($) {FormValidation.Validator.xxxx = {
        /**
         * @param {FormValidation.Base} validator The validator plugin instance
         * @param {jQuery} $field The jQuery object represents the field element
         * @param {Object} options The validator options
         * @returns {Boolean}
         */
        validate: function(validator, $field, options) {
			//TODO imp
        	options.regex = /&slsfsaf/;
        	return FormValidation.Validator.regex(validator, $field, options);
        }
    };
}(window.jQuery));