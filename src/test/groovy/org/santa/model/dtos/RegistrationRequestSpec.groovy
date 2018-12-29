package org.santa.model.dtos

class RegistrationRequestSpec extends JsonModelBase {

    def "should validate not empty fields"() {

        given: "an empty registration request"

        def request = new RegistrationRequest()

        when: "dto is validated"

        def violations = localValidatorBeanFactory.validate(request)

        then: "correct number of violations are returned"

        violations.size() == 3
    }

    def "should validate invalid email"() {

        given: "an invalid email"

        def invalid = "not an email"
        def request =
                new RegistrationRequest(
                        username: "alex",
                        password: "password",
                        email:  invalid)

        when: "dto is validated"

        def violations = localValidatorBeanFactory.validate(request)

        then: "correct violation is returned"

        violations[0].propertyPath.currentLeafNode.name == "email"
    }
}
