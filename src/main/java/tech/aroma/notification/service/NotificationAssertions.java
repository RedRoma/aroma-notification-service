/*
 * Copyright 2016 RedRoma, Inc.
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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.aroma.thrift.channels.AromaChannel;
import tech.aroma.thrift.events.Event;
import tech.aroma.thrift.events.EventType;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;

import static tech.sirwellington.alchemy.arguments.Arguments.*;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.equalTo;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;
import static tech.sirwellington.alchemy.arguments.assertions.NumberAssertions.greaterThan;

/**
 *
 * @author SirWellington
 */
@Internal
@NonInstantiable
public final class NotificationAssertions 
{
    private final static Logger LOG = LoggerFactory.getLogger(NotificationAssertions.class);

    private NotificationAssertions() throws IllegalAccessException
    {
        throw new IllegalAccessException("cannot instantiate");
    }
    
    public static AlchemyAssertion<AromaChannel> validAromaChannel()
    {
        return channel ->
        {
            checkThat(channel)
                .usingMessage("channel is null")
                .is(notNull());
            
            if (!isSet(channel))
            {
                throw new FailedAssertionException("AromaChannel has no value: " + channel);
            }
            
        };
    }
    
    private static boolean isSet(AromaChannel channel)
    {
        if (channel.isSetCustomChannel())
        {
            return true;
        }

        if (channel.isSetEmail())
        {
            return true;
        }

        if (channel.isSetSlackChannel())
        {
            return true;
        }

        if (channel.isSetSlackUsername())
        {
            return true;
        }

        if (channel.isSetIosDevice())
        {
            return true;
        }

        if (channel.isSetAndroidDevice())
        {
            return true;
        }

        return false;
    }

    
    public static AlchemyAssertion<Event> validEvent()
    {
        return event ->
        {
            checkThat(event)
                .usingMessage("event is missing")
                .is(notNull());
            
            checkThat(event.timestamp)
                .usingMessage("invalid timestamp: " + event.timestamp)
                .is(greaterThan(0L));
            
            checkThat(event.eventType)
                .is(validEventType());
        };
    }
    
    public static AlchemyAssertion<EventType> validEventType()
    {
        return type ->
        {
            checkThat(type)
                .usingMessage("missing eventType")
                .is(notNull());
            
            checkThat(type.isSet())
                .usingMessage("eventType is not set")
                .is(equalTo(true));
        };
    }
}
