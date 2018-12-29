package org.santa.model.dtos

class LoginRequestSpec extends JsonModelBase {

    def "should validate not empty fields"() {

        given: "an empty registration request"

        def request = new LoginRequest()

        when: "dto is validated"

        def violations = localValidatorBeanFactory.validate(request)

        then: "correct number of violations are returned"

        violations.size() == 2
    }
}
