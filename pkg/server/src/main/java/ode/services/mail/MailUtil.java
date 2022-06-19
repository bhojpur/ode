package ode.services.mail;

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

import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

/**
 * Methods for dealing with the preparation of JavaMail MIME messages.
 * The corresponding send methods of JavaMailSender will take care of the actual
 * creation of a MimeMessage instance. Used primarily by asynchronous Ice
 * services: {@code ode.cmd.mail.SendEmailRequestI}.
 */

public class MailUtil {

    private final static Logger log = LoggerFactory.getLogger(MailUtil.class);

    private static final long serialVersionUID = -1L;

    protected final String sender;

    protected final JavaMailSender mailSender;

    public MailUtil(String sender, JavaMailSender mailSender) {
        if (StringUtils.isBlank(sender)) {
            log.error("ode.mail.from is empty. Email notification won't be sent.");
        }
        this.sender = sender;
        this.mailSender = mailSender;
        log.info("JavaMail version is {}", InternetAddress.class.getPackage().getImplementationVersion());
    }

    /**
     * Helper method that returns value of <code>ode.mail.from</code>.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Main method which takes typical email fields as arguments, to prepare and
     * populate the given new MimeMessage instance and send.
     *
     * @param from
     *            email address message is sent from
     * @param to
     *            email address message is sent to
     * @param topic
     *            topic of the message
     * @param body
     *            body of the message
     * @param html
     *            flag determines the content type to apply.
     * @param ccrecipients
     *            list of email addresses message is sent as copy to
     * @param bccrecipients
     *            list of email addresses message is sent as blind copy to
     */
    public void sendEmail(final String from, final String to,
            final String topic, final String body, final boolean html,
            final List<String> ccrecipients, final List<String> bccrecipients) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {

                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setText(body, html);
                message.setFrom(from);
                message.setSubject(topic);
                message.setTo(to);
                if (ccrecipients != null && !ccrecipients.isEmpty()) {
                    message.setCc(ccrecipients.toArray(new String[ccrecipients
                            .size()]));
                }

                if (bccrecipients != null && !bccrecipients.isEmpty()) {
                    message.setCc(bccrecipients
                            .toArray(new String[bccrecipients.size()]));
                }
            }

        };

        this.mailSender.send(preparator);
    }

    /**
     * Overloaded method which takes typical email fields as arguments, to
     * prepare and populate the given new MimeMessage instance and send. Sender
     * of the email is loaded from ode.mail.from
     *
     * @param to
     *            email address message is sent to
     * @param topic
     *            topic of the message
     * @param body
     *            body of the message
     * @param html
     *            flag determines the content type to apply.
     * @param ccrecipients
     *            list of email addresses message is sent as copy to
     * @param bccrecipients
     *            list of email addresses message is sent as blind copy to
     */
    public void sendEmail(final String to, final String topic,
            final String body, final boolean html,
            final List<String> ccrecipients, final List<String> bccrecipients) {

        this.sendEmail(sender, to, topic, body, html, ccrecipients,
                bccrecipients);
    }

    /**
     * Helper Validate that this address conforms to the syntax rules of RFC
     * 822.
     *
     * @param email
     *            email address
     */
    public boolean validateEmail(String email) {
        boolean isValid = true;
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
        } catch (AddressException e) {
            isValid = false;
        }
        return isValid;
    }
}
