package com.example.myapplication

import com.example.myapplication.domain.LoginValidator
import org.junit.Assert.*
import org.junit.Test

class LoginValidatorTest {
    @Test fun validEmailAcceptsSurroundingSpaces() {
        assertNull(LoginValidator.emailError(" demo@nova.com "))
    }
    @Test fun invalidEmailsAreRejected() {
        listOf("", " ", "demo", "demo@nova", "de mo@nova.com", "demo@@nova.com").forEach {
            assertNotNull("Should reject $it", LoginValidator.emailError(it))
        }
    }
    @Test fun passwordRequiresAtLeastSixCharacters() {
        assertNotNull(LoginValidator.passwordError(""))
        assertNotNull(LoginValidator.passwordError("12345"))
        assertNotNull(LoginValidator.passwordError("      "))
        assertNull(LoginValidator.passwordError("Nova123"))
    }
}
