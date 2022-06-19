package ode.services.scheduler;

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

import java.util.HashMap;
import java.util.Map;

import ode.tools.spring.OnContextRefreshedEventListener;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.SchedulingException;

/**
 * Produces a
 * {@link Scheduler} which automatically loads all the triggers it can find.
 */
public class SchedulerFactoryBean extends
        org.springframework.scheduling.quartz.SchedulerFactoryBean implements
        ApplicationListener<ContextRefreshedEvent>,
        ApplicationContextAware {

    private final static Logger log = LoggerFactory
            .getLogger(SchedulerFactoryBean.class);

    private final Map<String, Trigger> triggers = new HashMap<String, Trigger>();

    private final static String JOB_DETAIL_KEY = "jobDetail";

    /**
     * Already subclassing another class, so re-using the handler here
     * in a somewhat awkward to re-use the code.
     */
    private final OnContextRefreshedEventListener handler =
            new OnContextRefreshedEventListener(true, Integer.MAX_VALUE) {

            @Override
            public void handleContextRefreshedEvent(ContextRefreshedEvent event) {
                handle(event);
            }
        };

    public void setApplicationContext(ApplicationContext ctx) {
        handler.setApplicationContext(ctx);
    }

    public void onApplicationEvent(ContextRefreshedEvent event) {
        handler.onApplicationEvent(event);
    }

    private void handle(ContextRefreshedEvent cre) {
        String[] names = cre.getApplicationContext().getBeanNamesForType(
                Trigger.class);

        for (String name : names) {
            if (triggers.containsKey(name)) {
                log.error("Scheduler already has trigger named: " + name);
                continue;
            }
            Trigger trigger = (Trigger) cre.getApplicationContext()
                    .getBean(name);
            registerTrigger(name, trigger);
        }
        restartIfNeeded();
    }

    /**
     * Registers a {@link Trigger}. A method like this should
     * really have protected visibility in the superclass.
     */
    protected void registerTrigger(String beanName, Trigger trigger) {
        try {
            Scheduler scheduler = getObject();
            triggers.put(beanName, trigger);
            JobDetail job = (JobDetail) trigger.getJobDataMap().get(JOB_DETAIL_KEY);
            scheduler.scheduleJob(job, trigger);
            log.debug(String.format("Registered trigger \"%s\": %s", beanName, trigger));
        } catch (SchedulerException se) {
            throw new RuntimeException(se);
        }
    }

    /**
     * Similar to the {@link #isRunning()} method, but properly handles the
     * situation where the {@link Scheduler} has been completely shutdown and
     * therefore must be replaced.
     */
    protected void restartIfNeeded() {
        if (!isRunning()) {
            try {
                start();
            } catch (SchedulingException se) {
                log.info("Replacing scheduler");
                try {
                    afterPropertiesSet();
                    if (!isRunning()) {
                        start();
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Failed to restart scheduler", e);
                }
            }
        }
    }

}
