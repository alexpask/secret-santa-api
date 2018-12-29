package org.santa.model.dtos

import com.fasterxml.jackson.databind.ObjectMapper
import org.hibernate.validator.HibernateValidator
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import spock.lang.Specification

/**
 * Provides helper methods for testing model serialization\/deserialization and
 * testing validation annotations.
 *
 **/
abstract class JsonModelBase extends Specification {

    protected def mapper = new ObjectMapper()
    protected def localValidatorBeanFactory = new LocalValidatorFactoryBean()

    void setup() {
        localValidatorBeanFactory.setProviderClass(HibernateValidator)
        localValidatorBeanFactory.afterPropertiesSet()
    }
}
