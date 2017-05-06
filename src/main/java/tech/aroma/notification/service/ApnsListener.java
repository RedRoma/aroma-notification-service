/*
 * Copyright 2017 RedRoma, Inc..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 
package tech.aroma.notification.service;


import com.notnoop.apns.ApnsDelegate;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.DeliveryError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author SirWellington
 */
final class ApnsListener implements ApnsDelegate 
{
    private final static Logger LOG = LoggerFactory.getLogger(ApnsListener.class);

    ApnsListener()
    {
    }

    @Override
    public void messageSent(ApnsNotification message, boolean resent)
    {
        LOG.debug("Confirming Message sent: {}", message.getIdentifier());
    }

    @Override
    public void messageSendFailed(ApnsNotification message, Throwable ex)
    {
        LOG.warn("Failed to send push notification message {}", message, ex);
    }

    @Override
    public void connectionClosed(DeliveryError e, int messageIdentifier)
    {
    }

    @Override
    public void cacheLengthExceeded(int newCacheLength)
    {
    }

    @Override
    public void notificationsResent(int resendCount)
    {
    }
}
