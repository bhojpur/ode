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
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Prevents creation of Spring beans based on read-only status.
 */
public class BeanInstantiationGuard implements BeanFactoryPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanInstantiationGuard.class);

    protected final String targetName;

    private final ReadOnlyStatus readOnly;

    private boolean isWriteDb = false;

    private boolean isWriteRepo = false;

    /**
     * Construct a bean instantiation guard.
     * @param readOnly the read-only status
     * @param targetName the name of a bean that needs to write
     */
    public BeanInstantiationGuard(ReadOnlyStatus readOnly, String targetName) {
        this.readOnly = readOnly;
        this.targetName = targetName;
    }

    /**
     * @param isWriteDb if the target bean needs to write to the database, defaults to {@code false}
     */
    public void setIsWriteDb(boolean isWriteDb) {
        this.isWriteDb = isWriteDb;
    }

    /**
     * @param isWriteRepo if the target bean needs to write to the repository, defaults to {@code false}
     */
    public void setIsWriteRepo(boolean isWriteRepo) {
        this.isWriteRepo = isWriteRepo;
    }

    /**
     * @return if the changes for read-only should be made
     */
    private boolean isTriggerConditionMet() {
        return isWriteDb && readOnly.isReadOnlyDb() ||
               isWriteRepo && readOnly.isReadOnlyRepo();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) {
        if (isTriggerConditionMet()) {
            setBeanDefinitionForReadOnly((BeanDefinitionRegistry) factory);
        }
    }

    /**
     * Act on the bean definition registry to make the target bean suitable for read-only mode.
     * @param registry the bean definition registry
     */
    protected void setBeanDefinitionForReadOnly(BeanDefinitionRegistry registry) {
        LOGGER.info("in read-only state so removing Spring bean named {}", targetName);
        registry.removeBeanDefinition(targetName);
    }
}
