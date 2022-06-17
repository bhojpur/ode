package ode.services.util;

// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Adjusts instantiation class of Spring beans based on read-only status.
 */
public class BeanInstantiationSubstituter extends BeanInstantiationGuard {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanInstantiationSubstituter.class);

    private final String substituteClass;

    /**
     * Construct a bean instantiation substituter.
     * @param readOnly the read-only status
     * @param targetName the name of a bean that needs to write
     * @param substituteClass the name of the class implementing the read-only variant of the bean
     */
    public BeanInstantiationSubstituter(ReadOnlyStatus readOnly, String targetName, String substituteClass) {
        super(readOnly, targetName);
        this.substituteClass = substituteClass;
    }

    @Override
    protected void setBeanDefinitionForReadOnly(BeanDefinitionRegistry registry) {
        LOGGER.info("in read-only state so setting Spring bean named {} to instantiate {}", targetName, substituteClass);
        registry.getBeanDefinition(targetName).setBeanClassName(substituteClass);
    }
}
