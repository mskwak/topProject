package com.daou.commons.validator;

import org.apache.commons.validator.routines.EmailValidator;

public class EmailValidatorTest {

	public static void main(String[] args) {
		String email = "kiho2.kim@tongyang";
		EmailValidator emailValidator = EmailValidator.getInstance();

		if(emailValidator.isValid(email)) {
			System.out.println("valid");
		} else {
			System.out.println("invalid");
		}

	}
}
